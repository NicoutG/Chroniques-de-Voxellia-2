package tools;

public class DurationTester {
    private static final int NB_MAX_TESTS = 10;
    private static long[] begin = new long[NB_MAX_TESTS];
    private static long[] totalTime = new long[NB_MAX_TESTS];
    private static int[] nbRepet = new int[NB_MAX_TESTS];

    public static void beginRecord(int index) {
        begin[index] = System.nanoTime();
    }

    public static void beginRecord() {
        beginRecord(0);
    }

    public static void endRecord(int index) {
        long end = System.nanoTime();
        nbRepet[index]++;
        totalTime[index] += end - begin[index];
    }

    public static void endRecord() {
        endRecord(0);
    }

    public static void initTotalTime(int index) {
        totalTime[index] = 0;
        nbRepet[index] = 0;
    }

    public static void initTotalTime() {
        initTotalTime(0);
    }

    public static void initAllTotalTime() {
        for (int i = 0; i < NB_MAX_TESTS; i++)
            initTotalTime(i);
    }

    public static double getDurationMs(int index) {
        return totalTime[index] / 1_000_000.0;
    }

    public static double getDurationMs() {
        return getDurationMs(0);
    }

    public static long getDurationNano(int index) {
        return totalTime[index];
    }

    public static long getDurationNano() {
        return getDurationNano(0);
    }

    public static double getDurationMsPerRepet(int index) {
        return totalTime[index] / (1_000_000.0 * nbRepet[index]);
    }

    public static double getDurationMsPerRepet() {
        return getDurationMsPerRepet(0);
    }

    public static double getDurationNanoPerRepet(int index) {
        return 1.0 * totalTime[index] / nbRepet[index];
    }

    public static double getDurationNanoPerRepet() {
        return getDurationNanoPerRepet(0);
    }
}
