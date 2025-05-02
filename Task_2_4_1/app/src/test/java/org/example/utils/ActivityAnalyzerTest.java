package org.example.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class ActivityAnalyzerTest {

    @Test
    void testAnalyzeWeeklyActivity() throws IOException, InterruptedException {
        String repoPath = "src/test/resources/cloned-repo";
        String startDate = "2025-01-01";
        String endDate = "2025-01-07";

        int commitCount = ActivityAnalyzer.analyzeWeeklyActivity(repoPath, startDate, endDate);
        assertTrue(commitCount >= 0);
    }

    @Test
    void testGetLastCommitDate() throws IOException, InterruptedException {
        String repoPath = "src/test/resources/cloned-repo";

        String lastCommitDate = ActivityAnalyzer.getLastCommitDate(repoPath);
        assertNotNull(lastCommitDate);
    }
}
