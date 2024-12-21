package org.example.enums;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private List<Assessment> assessments = new ArrayList<>();

    public void addAssessment(Assessment assessment) {
        assessments.add(assessment);
    }

    public List<Assessment> getAssessments() {
        return assessments;
    }
}
