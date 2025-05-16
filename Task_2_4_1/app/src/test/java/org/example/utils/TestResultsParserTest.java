package org.example.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.example.models.TestResults;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class TestResultsParserTest {

    @Test
    void testParseValidHtmlReport() throws IOException {
        String validHtmlPath = "src/test/resources/index.html";
        TestResults results = TestResultsParser.parseTestResults(validHtmlPath);

        assertNotNull(results);
        assertEquals(9, results.getPassed());
        assertEquals(0, results.getFailed());
        assertEquals(0, results.getSkipped());
    }

    @Test
    void testParseValidXmlReport() throws IOException {
        String validXmlPath = "src/test/resources/example.xml";
        TestResults results = TestResultsParser.parseTestResults(validXmlPath);

        assertNotNull(results);
        assertEquals(4, results.getPassed());
        assertEquals(0, results.getFailed());
        assertEquals(0, results.getSkipped());
    }
}
