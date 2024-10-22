package org.Task_1_1_4;

import java.util.List;

public interface Graph {
    void addVertex(int vertex);

    void removeVertex(int vertex);

    void addEdge(int from, int to);

    void removeEdge(int from, int to);

    List<Integer> getNeighbors(int vertex);

    void readFile(String filePath);

    String toString();

    boolean equals(Object obj);

    List<Integer> topologicalSort();

    void printGraph();
}
