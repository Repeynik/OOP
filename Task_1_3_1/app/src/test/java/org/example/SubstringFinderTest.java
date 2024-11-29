package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class SubstringFinderTest {

    private File tempDir;

    @BeforeEach
    void setUp() {
        tempDir = new File(System.getProperty("java.io.tmpdir"), "SubstringFinderTests");
        tempDir.mkdirs();
    }

    @AfterEach
    void tearDown() {
        deleteDirectory(tempDir);
    }

    private void deleteDirectory(File dir) {
        for (var file : dir.listFiles()) {
            if (file.isDirectory()) {
                deleteDirectory(file);
            } else {
                file.delete();
            }
        }
        dir.delete();
    }

    @Test
    void testSampleInput() throws IOException {
        var text = "абракадабра";
        var pattern = "бра";
        var file = createTempFile(text);
        var expected = List.of(1, 8);
        var actual = SubstringFinder.find(file.getAbsolutePath(), pattern);
        assertEquals(expected, actual);
    }

    @Test
    void testEmptyText() throws IOException {
        var text = "";
        var pattern = "бра";
        var file = createTempFile(text);
        var expected = new ArrayList<>();
        var actual = SubstringFinder.find(file.getAbsolutePath(), pattern);
        assertEquals(expected, actual);
    }

    @Test
    void testPatternNotPresent() throws IOException {
        var text = "абракадабра";
        var pattern = "xyz";
        var file = createTempFile(text);
        var expected = new ArrayList<>();
        var actual = SubstringFinder.find(file.getAbsolutePath(), pattern);
        assertEquals(expected, actual);
    }

    @Test
    void testPatternLongerThanText() throws IOException {
        var text = "abra";
        var pattern = "абракадабра";
        var file = createTempFile(text);
        var expected = new ArrayList<>();
        var actual = SubstringFinder.find(file.getAbsolutePath(), pattern);
        assertEquals(expected, actual);
    }

    @Test
    void testPatternSpanningChunks() throws IOException {
        var pattern = "spanningPattern";
        var text =
                "This is a test string where the pattern will spanningPattern be spanningPattern"
                        + " placed spanningPattern at multiple spanningPattern positions.";
        File file = createTempFile(text);

        var expected = new ArrayList<>();
        int index = text.indexOf(pattern);
        while (index != -1) {
            expected.add(index);
            index = text.indexOf(pattern, index + pattern.length());
        }

        var actual = SubstringFinder.find(file.getAbsolutePath(), pattern);
        assertEquals(expected, actual);
    }

    @Test
    void testLargeFile() throws IOException {
        var pattern = "repeatPattern";
        var sb = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            sb.append("This is a repeatPattern test.");
        }
        var text = sb.toString();
        var file = createTempFile(text);

        var expected = new ArrayList<>();
        int index = -1;
        while ((index = text.indexOf(pattern, index + 1)) != -1) {
            expected.add(index);
        }

        var actual = SubstringFinder.find(file.getAbsolutePath(), pattern);
        assertEquals(expected, actual);
    }

    @Test
    void testSmallFile() throws IOException {
        var pattern = "repeatPattern";
        var sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append("This is a repeatPattern test.");
        }
        var text = sb.toString();
        var file = createTempFile(text);

        var expected = new ArrayList<>();
        int index = -1;
        while ((index = text.indexOf(pattern, index + 1)) != -1) {
            expected.add(index);
        }

        var actual = SubstringFinder.find(file.getAbsolutePath(), pattern);
        assertEquals(expected, actual);
    }

    private File createTempFile(String content) throws IOException {
        var file = new File(tempDir, System.currentTimeMillis() + ".txt");
        try (var writer =
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            writer.write(content);
        }
        return file;
    }
}
