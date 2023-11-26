package util;

import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class DijkstraGraphUtil {

    private static final String INVALID_FORMAT_CONNECTION_ERROR_MESSAGE = "The following connection is in an invalid format: [%s]";

    private DijkstraGraphUtil() {
        // Empty constructor
    }

    private static Graph graph = new Graph();

    public static boolean isGraphEmpty() {
        return graph.getVertexList().isEmpty();
    }

    public static boolean isVertexExistent(String vertexName) {
        for (Vertex vertex : graph.getVertexList()) {
            if (vertex.getName() != null && vertex.getName().equals(vertexName)) return true;

        }

        System.out.println("Nonexistent vertex name");
        return false;
    }

    public static void addConnectionsByList(List<String> connectionList) {
        for (String line : connectionList) {
            String[] connectionData = line.split(";");

            if (connectionData.length != 3) {
                throw new IllegalArgumentException(String.format(INVALID_FORMAT_CONNECTION_ERROR_MESSAGE, line));
            }

            Vertex firstVertex = getOrCreateVertex(connectionData[0]);
            Vertex secondVertex = getOrCreateVertex(connectionData[1]);
            String weight = connectionData[2];

            try {
                buildNonDirectedConnection(firstVertex, secondVertex, weight);
            } catch (Exception e) {
                throw new IllegalArgumentException(String.format(INVALID_FORMAT_CONNECTION_ERROR_MESSAGE, line));
            }
        }
    }

    public static String getSmallestDistanceBetweenVertices(Vertex initialVertex, Vertex finalVertex) {
        if (initialVertex == null || finalVertex == null) {
            return "Unable to calculate distance between vertices";
        }

        Map<Vertex, Double> distances = new HashMap<>();
        Map<Vertex, Vertex> predecessors = new HashMap<>();

        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>(compareToVertexPriority(distances));

        setInitialValuesWhenCalculatingSmallestDistance(initialVertex, distances, predecessors, priorityQueue);
        calculateSmallestDistanceToVertexFromInitialVertex(distances, predecessors, priorityQueue);

        List<Vertex> path = new ArrayList<>();
        buildPathByPredecessorsFromFinalVertex(finalVertex, predecessors, path);

        List<Double> weights = new ArrayList<>();
        buildWeightsByPredecessorsFromFinalVertex(finalVertex, distances, predecessors, weights);

        StringBuilder result = buildSmallestDistanceToFinalVertexDescription(initialVertex, finalVertex, path);
        buildTotalWeightToFinalVertexDescription(result, weights);

        return result.toString();
    }

    private static StringBuilder buildSmallestDistanceToFinalVertexDescription(Vertex initialVertex, Vertex finalVertex, List<Vertex> path) {
        StringBuilder result = new StringBuilder(initialVertex.getName() + " to " + finalVertex.getName() + ": [");
        for (int i = path.size() -1; i >= 0; i--) {
            result.append(path.get(i).getName());
            if (i > 0) result.append(" -> ");
        }

        result.append("]");
        return result;
    }

    private static StringBuilder buildTotalWeightToFinalVertexDescription(StringBuilder result, List<Double> weights) {
        result.append(" - [");
        for (int i = weights.size() - 1; i > 0; i--) {
            result.append(weights.get(i));
            if (i > 1) result.append(",");
        }

        result.append("] = ").append(weights.isEmpty() ? "0.0" : weights.get(0));
        return result;
    }

    private static Comparator<? super Vertex> compareToVertexPriority(Map<Vertex, Double> distances) {
        return Comparator.comparingDouble(distances::get);
    }

    private static void setInitialValuesWhenCalculatingSmallestDistance(Vertex initialVertex, Map<Vertex, Double> distances, Map<Vertex, Vertex> predecessors, PriorityQueue<Vertex> priorityQueue) {
        for (Vertex vertex : graph.getVertexList()) {
            distances.put(vertex, Double.POSITIVE_INFINITY);
            predecessors.put(vertex, null);
        }

        distances.put(initialVertex, 0.0);
        priorityQueue.add(initialVertex);
    }

    private static void calculateSmallestDistanceToVertexFromInitialVertex(Map<Vertex, Double> distances, Map<Vertex, Vertex> predecessors, PriorityQueue<Vertex> priorityQueue) {
        while (!priorityQueue.isEmpty()) {
            Vertex actualVertex = priorityQueue.poll();

            for (Map.Entry<String, Double> connection : actualVertex.getConnections().entrySet()) {
                Vertex neighbor = getVertex(connection.getKey());
                double actualDistance = getDistanceToNeighborVertexFromInicialVertex(distances, actualVertex, connection);

                if (actualDistance < distances.get(neighbor)) {
                    distances.put(neighbor, actualDistance);
                    predecessors.put(neighbor, actualVertex);
                    priorityQueue.add(neighbor);
                }
            }
        }
    }

    private static void buildPathByPredecessorsFromFinalVertex(Vertex finalVertex, Map<Vertex, Vertex> predecessors, List<Vertex> path) {
        Vertex actualVertex = finalVertex;

        while (actualVertex != null) {
            path.add(actualVertex);
            actualVertex = predecessors.get(actualVertex);
        }
    }

    private static double getDistanceToNeighborVertexFromInicialVertex(Map<Vertex, Double> distances, Vertex actualVertex, Map.Entry<String, Double> connection) {
        return distances.get(actualVertex) + connection.getValue();
    }

    private static void buildWeightsByPredecessorsFromFinalVertex(Vertex finalVertex, Map<Vertex, Double> distances, Map<Vertex, Vertex> predecessors, List<Double> weights) {
        Vertex actualVertex = predecessors.get(finalVertex);
        Vertex sucessorVertex = finalVertex;

        weights.add(distances.get(sucessorVertex));

        while (actualVertex != null) {
            weights.add(distances.get(sucessorVertex) - distances.get(actualVertex));
            sucessorVertex = actualVertex;
            actualVertex = predecessors.get(actualVertex);
        }
    }

    private static void buildNonDirectedConnection(Vertex firstVertex, Vertex secondVertex, String weight) {
        Map<String, Double> connections = new HashMap<>();
        Map<String, Double> reverseConnections = new HashMap<>();

        connections.put(secondVertex.getName(), Double.parseDouble(weight));
        reverseConnections.put(firstVertex.getName(), Double.parseDouble(weight));

        graph.getWeights().add(Double.parseDouble(weight));

        firstVertex.getConnections().putAll(connections);
        secondVertex.getConnections().putAll(reverseConnections);
    }

    private static Vertex getOrCreateVertex(String vertexName) {
        Vertex vertex = getVertex(vertexName);
        if (vertex == null) {
            vertex = new Vertex(vertexName, new HashMap<>());
            graph.getVertexList().add(vertex);
        }
        return vertex;
    }

    public static Vertex getVertex(String vertexName) {
        for (Vertex vertex : graph.getVertexList()) {
            if (vertex.getName().equals(vertexName)) return vertex;
        }
        return null;
    }

    public static List<Double> buildStackByWeights() {
        List<Double> stack = new ArrayList<>();
        List<Double> weights = graph.getWeights();

        if (!weights.isEmpty()) {
            stack.addAll(weights);
            Collections.reverse(stack);
        }

        return stack;
    }

    public static List<Double> buildQueueByWeights() {
        List<Double> queue = new ArrayList<>(graph.getWeights());
        return queue;
    }
}
