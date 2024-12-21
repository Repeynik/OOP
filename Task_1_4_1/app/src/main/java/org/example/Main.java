package org.example;

import org.example.enums.FormOfStudy;
import org.example.enums.Grade;

public class Main {
    public static void main(String[] args) {
        var recordBook =
                new StudentRecordBook(
                        FormOfStudy.PAID, 3, "John Doe", "Group A", "Faculty of Science");

        recordBook.setGrade(1, "Mathematics", Grade.EXCELLENT);
        recordBook.setGrade(1, "Physics", Grade.EXCELLENT);

        recordBook.setGrade(2, "Chemistry", Grade.EXCELLENT);
        recordBook.setGrade(2, "Biology", Grade.EXCELLENT);

        recordBook.setGrade(3, "History", Grade.EXCELLENT);
        recordBook.setGrade(3, "Geography", Grade.EXCELLENT);
        recordBook.setGrade(3, "Literature", Grade.SATISFACTORY);

        recordBook.setGrade(4, "Economics", Grade.EXCELLENT);
        recordBook.setGrade(4, "Sociology", Grade.EXCELLENT);

        recordBook.setGrade(4, "NonExistentSubject", Grade.EXCELLENT);

        System.out.println("Student Name: " + recordBook.getName());
        System.out.println("Student Group: " + recordBook.getGroup());
        System.out.println("Student Faculty: " + recordBook.getFaculty());

        System.out.println("Current Average: " + recordBook.getCurrentAverage());
        System.out.println("Can switch to budget: " + recordBook.canSwitchToBudget());
        System.out.println("Can get red diploma: " + recordBook.canGetRedDiploma());
        System.out.println(
                "Can get increased scholarship: " + recordBook.canGetIncreasedScholarship());

        System.out.println("Assessments:");
        recordBook.getSessions().stream()
                .flatMap(session -> session.getAssessments().stream())
                .forEach(
                        assessment ->
                                System.out.println(
                                        "Subject: "
                                                + assessment.getSubjectName()
                                                + ", Grade: "
                                                + assessment.getGrade()
                                                + ", Type: "
                                                + assessment.getType()));
    }
}
