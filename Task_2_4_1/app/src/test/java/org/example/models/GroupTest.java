package org.example.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;

class GroupTest {
    @Test
    void testConstructorAndGetters() {
        Student student1 = new Student("user1", "Student One", "https://github.com/user1/repo");
        Student student2 = new Student("user2", "Student Two", "https://github.com/user2/repo");
        Group group = new Group("Group A", List.of(student1, student2));

        assertEquals("Group A", group.getName());
        assertEquals(2, group.getStudents().size());
        assertEquals("Student One", group.getStudents().get(0).getFullName());
    }

    @Test
    void testSetters() {
        Group group = new Group("Group A", List.of());
        group.setName("Group B");
        group.setStudents(List.of(new Student("user3", "Student Three", "https://github.com/user3/repo")));

        assertEquals("Group B", group.getName());
        assertEquals(1, group.getStudents().size());
        assertEquals("Student Three", group.getStudents().get(0).getFullName());
    }
}
