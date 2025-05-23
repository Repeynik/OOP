package org.example.checkers;

import org.example.models.StudentResult;
import org.example.utils.Configuration;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ReportGeneratorTest {

    @Test
    void testGenerateHtmlReport() throws IOException {
        Configuration config = new Configuration();
        config.loadFromGroovyFile("src/test/resources/courseConfig.groovy");
        String outputPath = "src/test/resources/report.html";

        assertDoesNotThrow(() -> ReportGenerator.generateHtmlReport(config, Collections.emptyList(), outputPath));
        assertTrue(new File(outputPath).exists());
    }

    @Test
    void testGenerateGroupSummary() throws IOException {
        Configuration config = new Configuration();
        config.loadFromGroovyFile("src/test/resources/courseConfig.groovy");
        String outputPath = "src/test/resources/group_summary.html";

        assertDoesNotThrow(() -> ReportGenerator.generateGroupSummary(config, Collections.emptyList(), outputPath));
        assertTrue(new File(outputPath).exists());
    }

    @Test
    void testGenerateCheckpointReports() throws IOException {
        Configuration config = new Configuration();
        config.loadFromGroovyFile("src/test/resources/courseConfig.groovy");
        String outputPath = "src/test/resources/checkpoint_reports.html";

        assertDoesNotThrow(() -> ReportGenerator.generateCheckpointReports(config, Collections.emptyList(), outputPath));
        assertTrue(new File(outputPath).exists());
    }
}
