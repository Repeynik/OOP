package org.Task_1_1_4;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class IncidenceMatrixGraphTest {

    @Test
    public void testAddVertex() {
        IncidenceMatrixGraph graph = new IncidenceMatrixGraph(5);
        graph.addVertex(1);
        assertTrue(graph.getNeighbors(1).isEmpty());
    }

    @Test
    public void testRemoveVertex() {
        IncidenceMatrixGraph graph = new IncidenceMatrixGraph(5);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);
        graph.removeVertex(1);
        assertTrue(graph.getNeighbors(1).isEmpty());
        assertTrue(graph.getNeighbors(2).isEmpty());
    }

    @Test
    public void testAddEdge() {
        IncidenceMatrixGraph graph = new IncidenceMatrixGraph(5);
        graph.addEdge(1, 2);
        assertEquals(List.of(2), graph.getNeighbors(1));
        assertEquals(List.of(1), graph.getNeighbors(2));
    }

    @Test
    public void testRemoveEdge() {
        IncidenceMatrixGraph graph = new IncidenceMatrixGraph(5);
        graph.addEdge(1, 2);
        graph.removeEdge(1, 2);
        assertTrue(graph.getNeighbors(1).isEmpty());
        assertTrue(graph.getNeighbors(2).isEmpty());
    }

    @Test
    public void testTopologicalSort() {
        IncidenceMatrixGraph graph = new IncidenceMatrixGraph(12);
        graph.addEdge(3, 8);
        graph.addEdge(3, 10);
        graph.addEdge(5, 11);
        graph.addEdge(7, 11);
        graph.addEdge(7, 8);
        graph.addEdge(8, 9);
        graph.addEdge(11, 2);
        graph.addEdge(11, 9);
        graph.addEdge(11, 10);

        List<Integer> sortedList = graph.topologicalSort();
        List<Integer> expectedList = Arrays.asList(3, 5, 7, 11, 8, 2, 10, 9);
        assertEquals(expectedList, sortedList);
    }
}
