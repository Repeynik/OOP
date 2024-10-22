package org.Task_1_1_4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class IncidenceMatrixGraph implements Graph {
    private int[][] incidenceMatrix;
    private List<Integer> vertices;
    private List<int[]> edges;

    public IncidenceMatrixGraph(int size) {
        incidenceMatrix = new int[size][size];
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
    }

    @Override
    public void addVertex(int vertex) {
        vertices.add(vertex);
        if (vertices.size() > incidenceMatrix.length) {
            int newSize = incidenceMatrix.length * 2;
            int[][] newIncidenceMatrix = new int[newSize][newSize];
            for (int i = 0; i < incidenceMatrix.length; i++) {
                System.arraycopy(
                        incidenceMatrix[i], 0, newIncidenceMatrix[i], 0, incidenceMatrix[i].length);
            }
            incidenceMatrix = newIncidenceMatrix;
        }
    }

    @Override
    public void removeVertex(int vertex) {
        int index = vertices.indexOf(vertex);
        if (index != -1) {
            vertices.remove(index);
            for (int i = 0; i < incidenceMatrix.length; i++) {
                incidenceMatrix[index][i] = 0;
                incidenceMatrix[i][index] = 0;
            }
        }
    }

    @Override
    public void addEdge(int from, int to) {
        if (!vertices.contains(from)) {
            vertices.add(from);
        }
        if (!vertices.contains(to)) {
            vertices.add(to);
        }
        int fromIndex = vertices.indexOf(from);
        int toIndex = vertices.indexOf(to);
        edges.add(new int[] {fromIndex, toIndex});
        incidenceMatrix[fromIndex][edges.size() - 1] = 1;
        incidenceMatrix[toIndex][edges.size() - 1] = 1;
    }

    @Override
    public void removeEdge(int from, int to) {
        int fromIndex = vertices.indexOf(from);
        int toIndex = vertices.indexOf(to);
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i)[0] == fromIndex && edges.get(i)[1] == toIndex) {
                incidenceMatrix[fromIndex][i] = 0;
                incidenceMatrix[toIndex][i] = 0;
                edges.remove(i);
                break;
            }
        }
    }

    @Override
    public List<Integer> getNeighbors(int vertex) {
        List<Integer> neighbors = new ArrayList<>();
        int index = vertices.indexOf(vertex);
        if (index != -1) {
            for (int j = 0; j < incidenceMatrix[0].length; j++) {
                if (incidenceMatrix[index][j] == 1) {
                    for (int i = 0; i < incidenceMatrix.length; i++) {
                        if (incidenceMatrix[i][j] == 1 && i != index) {
                            neighbors.add(vertices.get(i));
                        }
                    }
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
            List<int[]> edges = new ArrayList<>();
            vertices = new ArrayList<>();

            for (int i = 0; i < vertexCount; i++) {
                vertices.add(i);
            }

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                int from = Integer.parseInt(parts[0]);
                int to = Integer.parseInt(parts[1]);
                edges.add(new int[] {from, to});
            }

            incidenceMatrix = new int[vertexCount][edges.size()];
            for (int j = 0; j < edges.size(); j++) {
                int[] edge = edges.get(j);
                int fromIndex = vertices.indexOf(edge[0]);
                int toIndex = vertices.indexOf(edge[1]);
                incidenceMatrix[fromIndex][j] = 1;
                incidenceMatrix[toIndex][j] = 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return Arrays.deepToString(incidenceMatrix);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof IncidenceMatrixGraph)) return false;
        IncidenceMatrixGraph other = (IncidenceMatrixGraph) obj;
        return Arrays.deepEquals(incidenceMatrix, other.incidenceMatrix);
    }

    @Override
    public List<Integer> topologicalSort() {
        List<Integer> result = new ArrayList<>();
        int[] inDegree = new int[vertices.size()];
        Queue<Integer> queue = new LinkedList<>();

        for (int[] edge : edges) {
            int toIndex = edge[1];
            inDegree[toIndex]++;
        }

        for (int i = 0; i < vertices.size(); i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            result.add(vertices.get(vertex));

            for (int[] edge : edges) {
                if (edge[0] == vertex) {
                    int toIndex = edge[1];
                    inDegree[toIndex]--;
                    if (inDegree[toIndex] == 0) {
                        queue.add(toIndex);
                    }
                }
            }
        }

        if (result.size() != vertices.size()) {
            throw new IllegalStateException("Есть цикл");
        }

        return result;
    }

    @Override
    public void printGraph() {
        System.out.println("Матрица инцидентности:");
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.size(); j++) {
                System.out.print(incidenceMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
