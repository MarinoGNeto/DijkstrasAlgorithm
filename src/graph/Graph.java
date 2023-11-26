package graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    private List<Vertex> vertexList = new ArrayList<>();
    private List<Double> weights = new ArrayList<>();

    public List<Vertex> getVertexList() {
        return vertexList;
    }

    public List<Double> getWeights() {
        return weights;
    }
}
