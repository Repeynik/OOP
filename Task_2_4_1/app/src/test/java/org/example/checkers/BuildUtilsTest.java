package org.example.checkers;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BuildUtilsTest {

    @Test
    void testCompileProjectSuccess() throws IOException, InterruptedException {
        String validProjectPath = "src/test/resources/Task_1_2_2";
        new File(validProjectPath).mkdirs();
        assertTrue(BuildUtils.compileProject(validProjectPath));
    }

}
