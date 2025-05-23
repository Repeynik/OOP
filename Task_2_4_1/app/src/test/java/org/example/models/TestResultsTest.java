package org.example.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestResultsTest {

    @Test
    void testConstructorAndGetters() {
        TestResults results = new TestResults(5, 2, 1);
        assertEquals(5, results.getPassed());
        assertEquals(2, results.getFailed());
        assertEquals(1, results.getSkipped());
    }

}
