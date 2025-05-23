package org.example.models;

public class Student {
    private String githubUsername;
    private String fullName;
    private String repositoryUrl;

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public String getGithubUsername() {
        return githubUsername;
    }

    public String getFullName() {
        return fullName;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public void setGithubUsername(String githubUsername) {
        this.githubUsername = githubUsername;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Student(String githubUsername, String fullName, String repositoryUrl) {
        this.githubUsername = githubUsername;
        this.fullName = fullName;
        this.repositoryUrl = repositoryUrl;
    }
}