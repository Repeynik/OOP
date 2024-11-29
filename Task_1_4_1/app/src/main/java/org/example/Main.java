package org.example;

import org.example.enums.Assessment;
import org.example.enums.AssessmentType;
import org.example.enums.FormOfStudy;
import org.example.enums.Grade;
import org.example.enums.Session;

public class Main {
    public static void main(String[] args) {
        var recordBook = new StudentRecordBook(FormOfStudy.PAID, 3);

        var session1 = new Session();
        session1.addAssessment(new Assessment(AssessmentType.EXAM, Grade.EXCELLENT));
        session1.addAssessment(new Assessment(AssessmentType.DIFFERENTIAL_CREDIT, Grade.EXCELLENT));
        recordBook.addSession(session1);

        var session2 = new Session();
        session2.addAssessment(new Assessment(AssessmentType.EXAM, Grade.EXCELLENT));
        session2.addAssessment(new Assessment(AssessmentType.DIFFERENTIAL_CREDIT, Grade.EXCELLENT));
        recordBook.addSession(session2);

        var session3 = new Session();
        session3.addAssessment(new Assessment(AssessmentType.EXAM, Grade.EXCELLENT));
        session3.addAssessment(new Assessment(AssessmentType.DIFFERENTIAL_CREDIT, Grade.EXCELLENT));
        session3.addAssessment(new Assessment(AssessmentType.CONTROL_WORK, Grade.SATISFACTORY));
        recordBook.addSession(session3);

        System.out.println("Current Average: " + recordBook.getCurrentAverage());
        System.out.println("Can switch to budget: " + recordBook.canSwitchToBudget());
        System.out.println("Can get red diploma: " + recordBook.canGetRedDiploma());
        System.out.println(
                "Can get increased scholarship: " + recordBook.canGetIncreasedScholarship());
    }
}
