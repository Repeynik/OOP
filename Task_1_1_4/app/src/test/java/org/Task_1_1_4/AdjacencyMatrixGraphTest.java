package org.Task_1_1_4;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class AdjacencyMatrixGraphTest {

    @Test
    public void testAddVertex() {
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(5);
        graph.addVertex(1);
        assertTrue(graph.getNeighbors(1).isEmpty());
    }

    @Test
    public void testRemoveVertex() {
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(5);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);
        graph.removeVertex(1);
        assertTrue(graph.getNeighbors(1).isEmpty());
        assertTrue(graph.getNeighbors(2).isEmpty());
    }

    @Test
    public void testAddEdge() {
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(5);
        graph.addEdge(1, 2);
        assertEquals(List.of(2), graph.getNeighbors(1));
        assertEquals(List.of(), graph.getNeighbors(2));
    }

    @Test
    public void testRemoveEdge() {
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(5);
        graph.addEdge(1, 2);
        graph.removeEdge(1, 2);
        assertTrue(graph.getNeighbors(1).isEmpty());
    }

    @Test
    public void testTopologicalSort() {
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(12);
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
        List<Integer> expectedList = Arrays.asList(7, 5, 11, 2, 3, 10, 8, 9);
        assertEquals(expectedList, sortedList);
    }
}
