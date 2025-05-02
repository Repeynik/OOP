package org.example.checkers;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class StyleCheckerTest {

    // @Test
    // void testCheckStyleSuccess() throws IOException, InterruptedException {
    //     String validProjectPath = "src/test/resources/Task_1_2_2";
    //     new File(validProjectPath).mkdirs();
    //     assertDoesNotThrow(() -> StyleChecker.checkStyle(validProjectPath));
    // }

    @Test
    void testCheckStyleFailure() {
        String invalidProjectPath = "src/test/resources/invalid-project";
        assertThrows(IOException.class, () -> StyleChecker.checkStyle(invalidProjectPath));
    }
}
