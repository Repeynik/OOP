package org.example.checkers;

import org.example.models.TestResults;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TestRunnerTest {

    @Test
    void testRunTestsSuccess() throws IOException, InterruptedException {
        String validProjectPath = "src/test/resources/Task_1_2_2";
        new File(validProjectPath).mkdirs(); // Ensure the directory exists for the test
        TestResults results = TestRunner.runTests(validProjectPath);
        assertNotNull(results);
        assertTrue(results.getPassed() >= 0);
    }

    @Test
    void testRunTestsFailure() {
        String invalidProjectPath = "src/test/resources/invalid-project";
        assertThrows(RuntimeException.class, () -> TestRunner.runTests(invalidProjectPath));
    }
}
