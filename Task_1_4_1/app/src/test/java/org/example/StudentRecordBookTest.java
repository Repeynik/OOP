package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.example.enums.Assessment;
import org.example.enums.AssessmentType;
import org.example.enums.FormOfStudy;
import org.example.enums.Grade;
import org.example.enums.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentRecordBookTest {

    private StudentRecordBook recordBook;

    @BeforeEach
    void setUp() {
        recordBook = new StudentRecordBook(FormOfStudy.PAID, 1);
    }

    @Test
    void testGetCurrentAverage_WithVariousGrades_ShouldReturnCorrectAverage() {
        var session1 = new Session();
        session1.addAssessment(new Assessment(AssessmentType.EXAM, Grade.EXCELLENT));
        session1.addAssessment(new Assessment(AssessmentType.CONTROL_WORK, Grade.GOOD));
        session1.addAssessment(
                new Assessment(AssessmentType.DIFFERENTIAL_CREDIT, Grade.SATISFACTORY));
        recordBook.addSession(session1);

        double average = recordBook.getCurrentAverage();

        assertEquals(4.0, average, 0.001);
    }

    @Test
    void testCanSwitchToBudget_WithLessThanTwoSessions_ShouldReturnFalse() {
        var session1 = new Session();
        session1.addAssessment(new Assessment(AssessmentType.EXAM, Grade.EXCELLENT));
        recordBook.addSession(session1);

        boolean canSwitch = recordBook.canSwitchToBudget();

        assertFalse(canSwitch);
    }

    @Test
    void testCanSwitchToBudget_WithSatisfactoryInExams_ShouldReturnFalse() {
        var session1 = new Session();
        session1.addAssessment(new Assessment(AssessmentType.EXAM, Grade.EXCELLENT));
        recordBook.addSession(session1);

        var session2 = new Session();
        session2.addAssessment(new Assessment(AssessmentType.EXAM, Grade.SATISFACTORY));
        recordBook.addSession(session2);

        boolean canSwitch = recordBook.canSwitchToBudget();

        assertFalse(canSwitch);
    }

    @Test
    void testCanSwitchToBudget_NoSatisfactoryInExams_ShouldReturnTrue() {
        var session1 = new Session();
        session1.addAssessment(new Assessment(AssessmentType.EXAM, Grade.EXCELLENT));
        recordBook.addSession(session1);

        var session2 = new Session();
        session2.addAssessment(new Assessment(AssessmentType.EXAM, Grade.EXCELLENT));
        recordBook.addSession(session2);

        boolean canSwitch = recordBook.canSwitchToBudget();

        assertTrue(canSwitch);
    }

    @Test
    void testCanGetRedDiploma_QualificationWorkNotExcellent_ShouldReturnFalse() {

        var session1 = new Session();
        session1.addAssessment(new Assessment(AssessmentType.EXAM, Grade.EXCELLENT));
        recordBook.addSession(session1);

        recordBook.setQualificationWorkGrade(Grade.GOOD);

        boolean canGetDiploma = recordBook.canGetRedDiploma();

        assertFalse(canGetDiploma);
    }

    @Test
    void testCanGetRedDiploma_SatisfactoryInExams_ShouldReturnFalse() {
        var session1 = new Session();
        session1.addAssessment(new Assessment(AssessmentType.EXAM, Grade.SATISFACTORY));
        recordBook.addSession(session1);

        recordBook.setQualificationWorkGrade(Grade.EXCELLENT);

        boolean canGetDiploma = recordBook.canGetRedDiploma();

        assertFalse(canGetDiploma);
    }

    @Test
    void testCanGetRedDiploma_LessThan75PercentExcellent_ShouldReturnFalse() {
        var session1 = new Session();
        session1.addAssessment(new Assessment(AssessmentType.EXAM, Grade.EXCELLENT));
        session1.addAssessment(new Assessment(AssessmentType.DIFFERENTIAL_CREDIT, Grade.EXCELLENT));
        session1.addAssessment(new Assessment(AssessmentType.CONTROL_WORK, Grade.GOOD));
        recordBook.addSession(session1);

        recordBook.setQualificationWorkGrade(Grade.EXCELLENT);

        boolean canGetDiploma = recordBook.canGetRedDiploma();

        assertFalse(canGetDiploma);
    }

    @Test
    void testCanGetRedDiploma_AllConditionsMet_ShouldReturnTrue() {
        var session1 = new Session();
        session1.addAssessment(new Assessment(AssessmentType.EXAM, Grade.EXCELLENT));
        session1.addAssessment(new Assessment(AssessmentType.DIFFERENTIAL_CREDIT, Grade.EXCELLENT));
        recordBook.addSession(session1);

        recordBook.setQualificationWorkGrade(Grade.EXCELLENT);

        boolean canGetDiploma = recordBook.canGetRedDiploma();

        assertTrue(canGetDiploma);
    }

    @Test
    void testCanGetIncreasedScholarship_AllExcellent_ShouldReturnTrue() {
        recordBook = new StudentRecordBook(FormOfStudy.PAID, 1);

        var session1 = new Session();
        session1.addAssessment(new Assessment(AssessmentType.EXAM, Grade.EXCELLENT));
        session1.addAssessment(new Assessment(AssessmentType.CONTROL_WORK, Grade.EXCELLENT));
        recordBook.addSession(session1);

        boolean canGetScholarship = recordBook.canGetIncreasedScholarship();

        assertTrue(canGetScholarship);
    }

    @Test
    void testCanGetIncreasedScholarship_NonExcellentGrade_ShouldReturnFalse() {
        recordBook = new StudentRecordBook(FormOfStudy.PAID, 1);

        var session1 = new Session();
        session1.addAssessment(new Assessment(AssessmentType.EXAM, Grade.EXCELLENT));
        session1.addAssessment(new Assessment(AssessmentType.CONTROL_WORK, Grade.GOOD));
        recordBook.addSession(session1);

        boolean canGetScholarship = recordBook.canGetIncreasedScholarship();

        assertFalse(canGetScholarship);
    }

    @Test
    void testCanGetIncreasedScholarship_InvalidSemester_ShouldReturnFalse() {
        recordBook = new StudentRecordBook(FormOfStudy.PAID, 3);

        var session1 = new Session();
        session1.addAssessment(new Assessment(AssessmentType.EXAM, Grade.EXCELLENT));
        recordBook.addSession(session1);

        boolean canGetScholarship = recordBook.canGetIncreasedScholarship();

        assertFalse(canGetScholarship);
    }
}
