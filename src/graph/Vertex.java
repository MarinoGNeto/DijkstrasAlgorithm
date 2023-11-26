package graph;

import java.util.HashMap;
import java.util.Map;

public class Vertex {

    private String name;
    private Map<String, Double> connections = new HashMap<>();

    public Vertex(String name, Map<String, Double> connections) {
        this.name = name;
        this.connections = connections;
    }

    public String getName() {
        return name;
    }

    public Map<String, Double> getConnections() {
        return connections;
    }
}
