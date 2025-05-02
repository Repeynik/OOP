package org.example.models;

public class StudentResult {
    private Student student;
    private String taskName;
    private boolean buildSuccess;
    private boolean docSuccess;
    private boolean styleSuccess;
    private TestResults testResults;
    private int totalActivityScore;
    private String lastCommitDate;

    public StudentResult(Student student, String taskName, boolean buildSuccess, boolean docSuccess,
            boolean styleSuccess, TestResults testResults, int totalActivityScore, String lastCommitDate) {
        this.student = student;
        this.taskName = taskName;
        this.buildSuccess = buildSuccess;
        this.docSuccess = docSuccess;
        this.styleSuccess = styleSuccess;
        this.testResults = testResults;
        this.totalActivityScore = totalActivityScore;
        this.lastCommitDate = lastCommitDate;
    }

    public Student getStudent() {
        return student;
    }

    public String getTaskName() {
        return taskName;
    }

    public boolean isBuildSuccess() {
        return buildSuccess;
    }

    public boolean isDocSuccess() {
        return docSuccess;
    }

    public boolean isStyleSuccess() {
        return styleSuccess;
    }

    public TestResults getTestResults() {
        return testResults;
    }

    public int getTotalActivityScore() {
        return totalActivityScore;
    }

    public String getLastCommitDate() {
        return lastCommitDate;
    }
}