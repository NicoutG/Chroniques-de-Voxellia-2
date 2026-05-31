package audio;

import javax.sound.sampled.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class SimpleAudioSource {

    private SourceDataLine line;
    private AudioFormat format;

    private byte[] audioData;
    private Thread playThread;

    private volatile boolean playing = false;
    private volatile boolean finished = false;
    private volatile float volume = 1.0f;
    private final List<Runnable> finishCallbacks = new ArrayList<>();

    public SimpleAudioSource(InputStream audioStream) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(audioStream);
            this.format = ais.getFormat();

            this.audioData = readAllBytes(ais);
            ais.close();

        } catch (Exception e) {
            throw new RuntimeException("Failed to load audio", e);
        }
    }

    public SimpleAudioSource(byte[] bytes, AudioFormat format) {
        this.audioData = bytes;
        this.format = format;
    }

    /* =========================
       PLAY
       ========================= */

    public synchronized void play(boolean loop) {
        internalStop(); // reset propre

        try {
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            line = (SourceDataLine) AudioSystem.getLine(info);

            line.open(format);
            setVolume(volume);
            line.start();

            playing = true;
            finished = false;

            playThread = new Thread(() -> {
                try {
                    do {
                        line.write(audioData, 0, audioData.length);
                    } while (loop && playing);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    drainAndClose();
                }
            });

            playThread.start();

        } catch (Exception e) {
            throw new RuntimeException("Cannot play audio", e);
        }
    }

    /* =========================
       STOP
       ========================= */

    private synchronized void internalStop() {
        playing = false;

        if (line != null) {
            line.stop();
            line.flush();
            line.close();
        }

        if (playThread != null) {
            try {
                playThread.join(50);
            } catch (InterruptedException ignored) {}
        }

        finished = true;
    }

    public synchronized void stop() {
        internalStop();
        fireFinishCallbacks();
    }

    /* =========================
       STATE
       ========================= */

    public boolean isPlaying() {
        return playing && line != null && line.isActive();
    }

    public boolean isFinished() {
        return finished;
    }

    /* =========================
       VOLUME (instantané, sans lag)
       ========================= */

    public void setVolume(double volume) {
        setVolume((float)volume);
    }

    public void setVolume(float volume) {
        this.volume = Math.max(0f, volume);

        if (line == null) return;

        if (line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gain = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);

            float dB = (float) (20.0 * Math.log10(Math.max(0.0001f, this.volume)));

            dB = Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), dB));

            gain.setValue(dB);
        }
    }

    public float getVolume() {
        return volume;
    }

    public void onFinish(Runnable callback) {
        if (callback == null) return;

        synchronized (finishCallbacks) {
            finishCallbacks.add(callback);
        }
    }

    /* =========================
       INTERNAL
       ========================= */

    private void drainAndClose() {
        try {
            line.drain();
        } finally {
            line.stop();
            line.close();
            playing = false;
            finished = true;

            fireFinishCallbacks();
        }
    }

    private void fireFinishCallbacks() {
        List<Runnable> copy;

        synchronized (finishCallbacks) {
            copy = new ArrayList<>(finishCallbacks);
            finishCallbacks.clear();
        }

        for (Runnable r : copy) {
            try {
                r.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] readAllBytes(AudioInputStream ais) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];
        int read;

        while ((read = ais.read(buffer)) != -1) {
            baos.write(buffer, 0, read);
        }

        return baos.toByteArray();
    }
}