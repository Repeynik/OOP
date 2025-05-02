package org.example.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StudentTest {
    @Test
    void testConstructorAndGetters() {
        Student student = new Student("user1", "Student One", "https://github.com/user1/repo");

        assertEquals("user1", student.getGithubUsername());
        assertEquals("Student One", student.getFullName());
        assertEquals("https://github.com/user1/repo", student.getRepositoryUrl());
    }

    @Test
    void testSetters() {
        Student student = new Student("", "", "");
        student.setGithubUsername("user2");
        student.setFullName("Student Two");
        student.setRepositoryUrl("https://github.com/user2/repo");

        assertEquals("user2", student.getGithubUsername());
        assertEquals("Student Two", student.getFullName());
        assertEquals("https://github.com/user2/repo", student.getRepositoryUrl());
    }
}
