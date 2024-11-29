package org.Task_1_1_4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AdjacencyMatrixGraph implements Graph {
    private int[][] adjacencyMatrix;
    private List<Integer> vertices;

    public AdjacencyMatrixGraph(int size) {
        adjacencyMatrix = new int[size][size];
        vertices = new ArrayList<>();
    }

    @Override
    public void addVertex(int vertex) {
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);
            if (vertices.size() > adjacencyMatrix.length) {
                int newSize = adjacencyMatrix.length * 2;
                int[][] newAdjacencyMatrix = new int[newSize][newSize];
                for (int i = 0; i < adjacencyMatrix.length; i++) {
                    System.arraycopy(
                            adjacencyMatrix[i],
                            0,
                            newAdjacencyMatrix[i],
                            0,
                            adjacencyMatrix[i].length);
                }
                adjacencyMatrix = newAdjacencyMatrix;
            }
        }
    }

    @Override
    public void removeVertex(int vertex) {
        int index = vertices.indexOf(vertex);
        if (index != -1) {
            vertices.remove(index);
            for (int i = 0; i < adjacencyMatrix.length; i++) {
                adjacencyMatrix[index][i] = 0;
                adjacencyMatrix[i][index] = 0;
            }
        }
    }

    @Override
    public void addEdge(int from, int to) {
        if (!vertices.contains(from)) {
            addVertex(from);
        }
        if (!vertices.contains(to)) {
            addVertex(to);
        }
        int fromIndex = vertices.indexOf(from);
        int toIndex = vertices.indexOf(to);
        adjacencyMatrix[fromIndex][toIndex] = 1;
    }

    @Override
    public void removeEdge(int from, int to) {
        int fromIndex = vertices.indexOf(from);
        int toIndex = vertices.indexOf(to);
        if (fromIndex != -1 && toIndex != -1) {
            adjacencyMatrix[fromIndex][toIndex] = 0;
        }
    }

    @Override
    public List<Integer> getNeighbors(int vertex) {
        List<Integer> neighbors = new ArrayList<>();
        int index = vertices.indexOf(vertex);
        if (index != -1) {
            for (int i = 0; i < adjacencyMatrix[index].length; i++) {
                if (adjacencyMatrix[index][i] == 1) {
                    neighbors.add(vertices.get(i));
                }
            }
        }
        return neighbors;
    }

    @Override
    public void readFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int vertexCount = Integer.parseInt(br.readLine());

            for (int i = 0; i < vertexCount; i++) {
                addVertex(i);
            }

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                int from = Integer.parseInt(parts[0]);
                int to = Integer.parseInt(parts[1]);
                addEdge(from, to);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return Arrays.deepToString(adjacencyMatrix);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AdjacencyMatrixGraph)) return false;
        AdjacencyMatrixGraph other = (AdjacencyMatrixGraph) obj;
        return Arrays.deepEquals(adjacencyMatrix, other.adjacencyMatrix);
    }

    @Override
    public List<Integer> topologicalSort() {
        boolean[] visited = new boolean[vertices.size()];
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < vertices.size(); i++) {
            if (!visited[i]) {
                topologicalSortUtil(i, visited, stack);
            }
        }

        List<Integer> sortedList = new ArrayList<>();
        while (!stack.isEmpty()) {
            sortedList.add(vertices.get(stack.pop()));
        }

        return sortedList;
    }

    private void topologicalSortUtil(int v, boolean[] visited, Stack<Integer> stack) {
        visited[v] = true;

        for (int i = 0; i < adjacencyMatrix[v].length; i++) {
            if (adjacencyMatrix[v][i] == 1) {
                if (!visited[i]) {
                    topologicalSortUtil(i, visited, stack);
                }
            }
        }

        stack.push(v);
    }

    @Override
    public void printGraph() {
        System.out.println("Матрица смежности:");
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.size(); j++) {
                System.out.print(adjacencyMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
