package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SubstringFinder {

    public static List<Integer> find(final String filePath, final String substring)
            throws IOException {
        final List<Integer> indices = new ArrayList<>();

        final int[] prefix = buildPrefix(substring);
        final int overlap = substring.length() - 1;

        try (var is = new FileInputStream(filePath);
                var reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            final char[] buffer = new char[4096];
            int read;
            int totalRead = 0;
            final var overlapBuffer = new StringBuilder();

            while ((read = reader.read(buffer)) != -1) {
                final var chunk = new String(buffer, 0, read);
                final var data = overlapBuffer.toString() + chunk;

                int matchStart = 0;
                while (matchStart <= data.length() - substring.length()) {
                    final int match = kmpSearch(data, substring, prefix, matchStart);
                    if (match != -1) {
                        indices.add(match + totalRead);
                        matchStart = match + 1;
                    } else {
                        break;
                    }
                }

                if (data.length() > overlap) {
                    overlapBuffer.delete(0, overlapBuffer.length());
                    overlapBuffer.append(data.substring(data.length() - overlap));
                } else {
                    overlapBuffer.delete(0, overlapBuffer.length());
                    overlapBuffer.append(data);
                }

                totalRead += data.length() - overlapBuffer.length();
            }

            final var remainingData = overlapBuffer.toString();
            int matchStart = 0;
            while (matchStart <= remainingData.length() - substring.length()) {
                final int match = kmpSearch(remainingData, substring, prefix, matchStart);
                if (match != -1) {
                    indices.add(match + totalRead);
                    matchStart = match + 1;
                } else {
                    break;
                }
            }
        }

        return indices;
    }

    private static int[] buildPrefix(final String pattern) {
        final int[] prefix = new int[pattern.length()];
        int j = 0;
        for (int i = 1; i < pattern.length(); i++) {
            while (j > 0 && pattern.charAt(i) != pattern.charAt(j)) {
                j = prefix[j - 1];
            }
            if (pattern.charAt(i) == pattern.charAt(j)) {
                j++;
                prefix[i] = j;
            }
        }
        return prefix;
    }

    private static int kmpSearch(
            final String text, final String pattern, final int[] prefix, final int startIndex) {
        int i = startIndex;
        int j = 0;
        while (i < text.length() && j < pattern.length()) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            } else {
                if (j != 0) {
                    j = prefix[j - 1];
                } else {
                    i++;
                }
            }
        }
        if (j == pattern.length()) {
            return i - j;
        } else {
            return -1;
        }
    }
}
