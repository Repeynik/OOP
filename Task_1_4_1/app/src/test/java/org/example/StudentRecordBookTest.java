package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.example.enums.FormOfStudy;
import org.example.enums.Grade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentRecordBookTest {

    private StudentRecordBook recordBook;

    @BeforeEach
    void setUp() {
        recordBook =
                new StudentRecordBook(
                        FormOfStudy.PAID, 4, "John Doe", "23216", "FIT");
    }

    @Test
    void testGetCurrentAverage() {
        recordBook.setGrade(1, "Mathematics", Grade.EXCELLENT);
        recordBook.setGrade(1, "Physics", Grade.GOOD);
        recordBook.setGrade(2, "Chemistry", Grade.SATISFACTORY);

        double average = recordBook.getCurrentAverage();

        assertEquals(4.0, average, 0.001);
    }

    @Test
    void testCanSwitchToBudget() {
        recordBook.setGrade(1, "Mathematics", Grade.EXCELLENT);
        recordBook.setGrade(1, "Physics", Grade.EXCELLENT);

        boolean canSwitch = recordBook.canSwitchToBudget();

        assertFalse(canSwitch);
    }

    @Test
    void testCanSwitchToBudget_WithSatisfactoryInExams() {
        recordBook.setGrade(1, "Mathematics", Grade.EXCELLENT);
        recordBook.setGrade(1, "Physics", Grade.EXCELLENT);
        recordBook.setGrade(2, "Chemistry", Grade.SATISFACTORY);
        recordBook.setGrade(2, "Biology", Grade.EXCELLENT);

        boolean canSwitch = recordBook.canSwitchToBudget();

        assertFalse(canSwitch);
    }

    @Test
    void testCanSwitchToBudget_NoSatisfactoryInExams() {
        recordBook.setGrade(1, "Mathematics", Grade.EXCELLENT);
        recordBook.setGrade(1, "Physics", Grade.EXCELLENT);
        recordBook.setGrade(2, "Chemistry", Grade.EXCELLENT);
        recordBook.setGrade(2, "Biology", Grade.EXCELLENT);
        recordBook.setGrade(3, "History", Grade.EXCELLENT);
        recordBook.setGrade(3, "Geography", Grade.EXCELLENT);
        recordBook.setGrade(3, "Literature", Grade.EXCELLENT);

        boolean canSwitch = recordBook.canSwitchToBudget();

        assertTrue(canSwitch);
    }

    @Test
    void testCanGetRedDiploma_QualificationWorkNotExcellent() {
        recordBook.setGrade(1, "Mathematics", Grade.EXCELLENT);
        recordBook.setGrade(1, "Physics", Grade.EXCELLENT);
        recordBook.setQualificationWorkGrade(Grade.GOOD, "Qualification Work");

        boolean canGetDiploma = recordBook.canGetRedDiploma();

        assertFalse(canGetDiploma);
    }

    @Test
    void testCanGetRedDiploma_SatisfactoryInExams() {
        recordBook.setGrade(1, "Mathematics", Grade.SATISFACTORY);
        recordBook.setGrade(1, "Physics", Grade.EXCELLENT);
        recordBook.setQualificationWorkGrade(Grade.EXCELLENT, "Qualification Work");

        boolean canGetDiploma = recordBook.canGetRedDiploma();

        assertFalse(canGetDiploma);
    }

    @Test
    void testCanGetRedDiploma_LessThan75PercentExcellent() {
        recordBook.setGrade(1, "Mathematics", Grade.EXCELLENT);
        recordBook.setGrade(1, "Physics", Grade.EXCELLENT);
        recordBook.setGrade(2, "Chemistry", Grade.GOOD);
        recordBook.setQualificationWorkGrade(Grade.EXCELLENT, "Qualification Work");

        boolean canGetDiploma = recordBook.canGetRedDiploma();

        assertFalse(canGetDiploma);
    }

    @Test
    void testCanGetRedDiploma() {
        recordBook.setGrade(1, "Mathematics", Grade.EXCELLENT);
        recordBook.setGrade(1, "Physics", Grade.EXCELLENT);
        recordBook.setGrade(2, "Chemistry", Grade.EXCELLENT);
        recordBook.setGrade(2, "Biology", Grade.EXCELLENT);
        recordBook.setQualificationWorkGrade(Grade.EXCELLENT, "Qualification Work");

        boolean canGetDiploma = recordBook.canGetRedDiploma();

        assertTrue(canGetDiploma);
    }

    @Test
    void testCanGetIncreasedScholarship_AllExcellent() {
        recordBook.setGrade(4, "Philosophy", Grade.EXCELLENT);
        recordBook.setGrade(4, "Psychology", Grade.EXCELLENT);

        boolean canGetScholarship = recordBook.canGetIncreasedScholarship();

        assertTrue(canGetScholarship);
    }

    @Test
    void testCanGetIncreasedScholarship_NonExcellentGrade() {
        recordBook.setGrade(4, "Philosophy", Grade.EXCELLENT);
        recordBook.setGrade(4, "Psychology", Grade.GOOD);

        boolean canGetScholarship = recordBook.canGetIncreasedScholarship();

        assertTrue(canGetScholarship);
    }

    @Test
    void testCanGetIncreasedScholarship() {
        recordBook =
                new StudentRecordBook(
                        FormOfStudy.PAID, 7, "John Doe", "Group A", "Faculty of Science");

        boolean canGetScholarship = recordBook.canGetIncreasedScholarship();

        assertFalse(canGetScholarship);
    }
}
