package org.example.models;

public class TestResults {
    private int passed;
    private int failed;
    private int skipped;

    public TestResults(int passed, int failed, int skipped) {
        this.passed = passed;
        this.failed = failed;
        this.skipped = skipped;
    }

    public int getPassed() {
        return passed;
    }

    public int getFailed() {
        return failed;
    }

    public int getSkipped() {
        return skipped;
    }
}