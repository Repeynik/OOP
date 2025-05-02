package org.example.checkers;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class DocumentationGeneratorTest {

    @Test
    void testGenerateDocumentationSuccess() {
        String validProjectPath = "src/test/resources/Task_1_2_2";
        new File(validProjectPath).mkdirs(); // Ensure the directory exists for the test
        assertTrue(DocumentationGenerator.generate(validProjectPath));
    }

    @Test
    void testGenerateDocumentationFailure() {
        String invalidProjectPath = "src/test/resources/invalid-project";
        assertFalse(DocumentationGenerator.generate(invalidProjectPath));
    }
}
