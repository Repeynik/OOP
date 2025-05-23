package org.example.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.example.models.Student;
import org.example.models.StudentResult;
import org.junit.jupiter.api.Test;

import java.util.List;

class RepositoryProcessorTest {

    @Test
    void testFilterStudentsToCheck() {
        Configuration config = new Configuration();
        config.loadFromGroovyFile("src/test/resources/courseConfig.groovy");

        List<Student> studentsToCheck = RepositoryProcessor.filterStudentsToCheck(config);

        assertNotNull(studentsToCheck);
        assertEquals(1, studentsToCheck.size());
        assertEquals("Savushkina Alena", studentsToCheck.get(0).getFullName());
    }

    @Test
    void testProcessRepositories() {
        Configuration config = new Configuration();
        config.loadFromGroovyFile("src/test/resources/courseConfig.groovy");

        List<Student> studentsToCheck = RepositoryProcessor.filterStudentsToCheck(config);

        List<StudentResult> results = RepositoryProcessor.processRepositories(config, false, false, studentsToCheck);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals("Savushkina Alena", results.get(0).getStudent().getFullName());
    }
}
