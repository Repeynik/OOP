package org.Task_1_1_4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AdjacencyListGraph implements Graph {
    private Map<Integer, List<Integer>> adjacencyList;

    public AdjacencyListGraph() {
        adjacencyList = new HashMap<>();
    }

    @Override
    public void addVertex(int vertex) {
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    @Override
    public void removeVertex(int vertex) {
        adjacencyList.remove(vertex);
        for (List<Integer> neighbors : adjacencyList.values()) {
            neighbors.remove((Integer) vertex);
        }
    }

    @Override
    public void addEdge(int from, int to) {
        adjacencyList.putIfAbsent(from, new ArrayList<>());
        adjacencyList.putIfAbsent(to, new ArrayList<>());
        adjacencyList.get(from).add(to);
    }

    @Override
    public void removeEdge(int from, int to) {
        List<Integer> neighbors = adjacencyList.get(from);
        if (neighbors != null) {
            neighbors.remove((Integer) to);
        }
    }

    @Override
    public List<Integer> getNeighbors(int vertex) {
        return adjacencyList.getOrDefault(vertex, new ArrayList<>());
    }

    @Override
    public void readFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                int from = Integer.parseInt(parts[0]);
                int to = Integer.parseInt(parts[1]);
                addVertex(from);
                addVertex(to);
                addEdge((Integer) from, (Integer) to);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return adjacencyList.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AdjacencyListGraph)) return false;
        AdjacencyListGraph other = (AdjacencyListGraph) obj;
        return Objects.equals(adjacencyList, other.adjacencyList);
    }

    @Override
    public List<Integer> topologicalSort() {
        Map<Integer, Integer> inDegree = new HashMap<>();
        for (Integer vertex : adjacencyList.keySet()) {
            inDegree.put(vertex, 0);
        }

        for (List<Integer> neighbors : adjacencyList.values()) {
            for (Integer neighbor : neighbors) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (Map.Entry<Integer, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        List<Integer> sortedList = new ArrayList<>();
        while (!queue.isEmpty()) {
            Integer vertex = queue.poll();
            sortedList.add(vertex);

            for (Integer neighbor : adjacencyList.get(vertex)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        return sortedList;
    }

    @Override
    public void printGraph() {
        System.out.println("Список смежности:");
        for (Integer vertex : adjacencyList.keySet()) {
            System.out.print(vertex + ": ");
            for (Integer neighbor : adjacencyList.get(vertex)) {
                System.out.print(neighbor + " ");
            }
            System.out.println();
        }
    }
}
