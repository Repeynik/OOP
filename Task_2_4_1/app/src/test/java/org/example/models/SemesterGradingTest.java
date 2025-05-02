package org.example.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SemesterGradingTest {
    @Test
    void testConstructorAndGetters() {
        SemesterGrading grading = new SemesterGrading(13, 10, 7);

        assertEquals(13, grading.getExellent());
        assertEquals(10, grading.getGood());
        assertEquals(7, grading.getSatisfactory());
    }

    @Test
    void testCalculateSemesterGrade() {
        SemesterGrading grading = new SemesterGrading(13, 10, 7);

        assertEquals("Отлично", grading.calculateSemesterGrade(14));
        assertEquals("Хорошо", grading.calculateSemesterGrade(11));
        assertEquals("Удовлетворительно", grading.calculateSemesterGrade(8));
        assertEquals("Неудовлетворительно", grading.calculateSemesterGrade(6));
    }
}
