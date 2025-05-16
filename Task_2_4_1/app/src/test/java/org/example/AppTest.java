package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.File;

class AppTest {

    @Test
    void testMainWithPullMode() {
        String configFilePath = "src/test/resources/courseConfig.groovy";
        String outputReportPath = "src/test/resources/outputReport.html";
        String[] args = {configFilePath, outputReportPath, "pull"};

        assertDoesNotThrow(() -> App.main(args));

        File reportFile = new File(outputReportPath);
        assertTrue(reportFile.exists());
    }

    @Test
    void testMainWithRecheckMode() {
        String configFilePath = "src/test/resources/courseConfig.groovy";
        String outputReportPath = "src/test/resources/outputReport.html";
        String[] args = {configFilePath, outputReportPath, "recheck"};

        assertDoesNotThrow(() -> App.main(args));

        File reportFile = new File(outputReportPath);
        assertTrue(reportFile.exists());
    }

}
