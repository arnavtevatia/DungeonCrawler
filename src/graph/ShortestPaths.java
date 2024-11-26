package graph;

import datastructures.PQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This object computes and remembers shortest paths through a weighted, directed graph with
 * nonnegative weights. Once shortest paths are computed from a specified source vertex, it allows
 * querying the distance to arbitrary vertices and the best paths to arbitrary destination
 * vertices.
 * <p>
 * Types Vertex and Edge are parameters, so their operations are supplied by a model object supplied
 * to the constructor.
 */
public class ShortestPaths<Vertex, Edge> {

    /**
     * The model for treating types Vertex and Edge as forming a weighted directed graph.
     */
    private final WeightedDigraph<Vertex, Edge> graph;

    /**
     * The distance to each vertex from the source.
     */
    private Map<Vertex, Double> distances;

    /**
     * The incoming edge for the best path to each vertex from the source vertex.
     */
    private Map<Vertex, Edge> bestEdges;

    /**
     * Creates: a single-source shortest-path finder for a weighted graph.
     *
     * @param graph The model that supplies all graph operations.
     */
    public ShortestPaths(WeightedDigraph<Vertex, Edge> graph) {
        this.graph = graph;
    }

    /**
     * Effect: Computes the best paths from a given source vertex, which can then be queried using
     * bestPath().
     */
    public void singleSourceDistances(Vertex source) {
        assert source != null;
        // Implementation constraint: use Dijkstra's single-source shortest paths algorithm.
        PQueue<Vertex> frontier = new PQueue<>() {
            @Override
            public String toString() {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public void add(Vertex vertex, double priority) throws IllegalArgumentException {

            }

            @Override
            public Vertex peek() {
                return null;
            }

            @Override
            public Vertex extractMin() {
                return null;
            }

            @Override
            public double getPriority(Vertex element) {
                return 0;
            }

            @Override
            public void changePriority(Vertex vertex, double p) {

            }
        };
        distances = new HashMap<>();
        bestEdges = new HashMap<>();
        // First, we can add the source vertex to the frontier (0 distance to itself).
        frontier.add(source, 0);
        distances.put(source, 0.);

        // Loop until the entire graph is traversed and 'settled.'
        while (!frontier.isEmpty()) {
            // Start by taking out the smallest-distance vertex in the frontier.
            Vertex toAnalyze = frontier.extractMin();

            // Iterate over each neighboring edge from that vertex.
            for (Edge edge : graph.outgoingEdges(toAnalyze)) {
                // Grab the vertex on the other side of the edge.
                Vertex neighboringVertex = graph.dest(edge);

                // Calculate the total distance to this vertex through the source vertex.
                double distance = distances.get(toAnalyze) + graph.weight(edge);

                // Check whether a new distance or lesser distance has been found.
                if (!distances.containsKey(neighboringVertex) ||
                        distances.get(neighboringVertex) > distance) {
                    distances.put(neighboringVertex, distance);
                    bestEdges.put(neighboringVertex, edge);

                    // Either add to the frontier if undiscovered or update priority.
                    try {
                        frontier.add(neighboringVertex, distance);
                    } catch (IllegalArgumentException e) {
                        frontier.changePriority(neighboringVertex, distance);
                    }
                }
            }
        }
    }

    /**
     * Returns: the distance from the source vertex to the given vertex. Requires: distances have
     * been computed from a source vertex, and vertex v is reachable from that vertex.
     */
    public double getDistance(Vertex v) {
        assert !distances.isEmpty() : "Must run singleSourceDistances() first";
        Double d = distances.get(v);
        assert d != null : "v not reachable from source";
        return d;
    }

    /**
     * Returns: the best path from the source vertex to a given target vertex. The path is
     * represented as a list of edges. Requires: singleSourceDistances() has already been used to
     * compute best paths, and vertex target is reachable from that source.
     */
    public List<Edge> bestPath(Vertex target) {
        assert !bestEdges.isEmpty() : "Must run singleSourceDistances() first";
        LinkedList<Edge> path = new LinkedList<>();
        Vertex v = target;
        while (true) {
            Edge e = bestEdges.get(v);
            if (e == null) {
                break; // must be the source vertex (assuming target is reachable)
            }
            path.addFirst(e);
            v = graph.source(e);
        }
        return path;
    }
}
