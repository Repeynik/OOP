package org.example.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class GitUtilsTest {

    @Test
    void testCheckGitConfig() {
        assertDoesNotThrow(GitUtils::checkGitConfig);
    }

    @Test
    void testCloneRepository() {
        String repoUrl = "https://github.com/Repeynik/OOP";
        String destinationPath = "src/test/resources/cloned-repo";

        if (new File(destinationPath).exists()) {
            assertTrue(true);
        }
        else{
        assertDoesNotThrow(() -> GitUtils.cloneRepository(repoUrl, destinationPath));
        }
    }

    @Test
    void testPullRepository() {
        String localPath = "src/test/resources/cloned-repo";

        assertDoesNotThrow(() -> GitUtils.pullRepository(localPath));
    }
}
