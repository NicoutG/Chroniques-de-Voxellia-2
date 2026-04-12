package audio;

public interface ISoundType {
    String getPath();
    boolean isLooping();
    double getVolume();
    boolean isAmbient();
}
