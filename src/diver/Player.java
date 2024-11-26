package diver;

import datastructures.PQueue;
import game.*;
import graph.WeightedDigraph;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

// Player.java
// package diver;

import java.util.*;

public class Player implements Navigator{
    private String name;
    private long currentLocation;
    private int stepsRemaining;
    private int health;
    private int damage;
    private List<String> inventory;

    // Constructor for the player
    public Player(long startLocation) {
        this.name = "Default";
        this.currentLocation = startLocation;
        this.health = 100; // starting health
        this.stepsRemaining = 50;
        this.damage = 0; // limit on how many steps player can take
        this.inventory = new ArrayList<>();
    }

    // Constructor with name, health, and damage
    public Player(String name, int health, int damage, long startLocation) {
        this.name = name;
        this.currentLocation = startLocation;
        this.health = health; // starting health
        this.damage = damage; // limit on how many steps player can take
        this.inventory = new ArrayList<>();
        this.stepsRemaining = 100;
    }

    // Get the current location of the player
    public long currentLocation() {
        return currentLocation;
    }

    // Move to a new location (tile)
    public void moveTo(long newLocation) {
        this.currentLocation = newLocation;
        this.stepsRemaining--;
    }

    // Get the current distance to the exit or treasure
    public int distanceToExit() {
        // Placeholder, replace with actual logic to compute distance to the exit
        return 10;
    }

    // Get a list of current neighbors (other tiles around the player)
    public Collection<NodeStatus> neighbors() {
        // Placeholder, replace with logic to get neighboring tiles
        return new ArrayList<>();
    }

    // Collect an item (add it to the inventory)
    public void collectItem(String item) {
        inventory.add(item);
    }

    // Take damage from an enemy
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            this.health = 0;
            // Game Over logic here
        }
    }

    // Check if the player can take more steps
    public boolean canMove() {
        return stepsRemaining > 0;
    }

    // Add logic for player combat, treasure collection, and health
    public boolean isAlive() {
        return health > 0;
    }

    // Inventory management
    public List<String> getInventory() {
        return inventory;
    }

    // Check the player's current health
    public int getHealth() {
        return health;
    }

    @Override
    public void combat() {

    }

    @Override
    public void updateGameState() {

    }

    @Override
    public void seek(SeekState state) {

    }

    @Override
    public void scram(ScramState state) {

    }

    @Override
    public void generateDungeon() {

    }

    @Override
    public void startGame() {

    }

    @Override
    public void gameLoop() {

    }

    @Override
    public void movePlayer() {

    }
}



/**
 * This object computes and remembers shortest paths through a weighted, directed graph with
 * nonnegative weights. Once shortest paths are computed from a specified source vertex, it allows
 * querying the distance to arbitrary vertices and the best paths to arbitrary destination
 * vertices, as well as the closest coin and relatively close coin of highest value.
 */
class ScramShortestPaths {

    /**
     * The model for treating types Vertex and Edge as forming a weighted directed graph.
     */
    private final WeightedDigraph<Node, Edge> graph;

    /**
     * The distance to each vertex from the source.
     */
    private Map<Node, Double> distances;

    /**
     * The incoming edge for the best path to each vertex from the source vertex.
     */
    private Map<Node, Edge> bestEdges;

    /**
     * Tracks the distance to each coin in the map from the source vertex.
     */
    private TreeMap<Double, TreeSet<Node>> coinDistances;

    /**
     * Creates: a single-source shortest-path finder for a weighted graph.
     *
     * @param graph The model that supplies all graph operations.
     */
    public ScramShortestPaths(WeightedDigraph<Node, Edge> graph) {
        this.graph = graph;
    }

    /**
     * Effect: Computes the best paths from a given source vertex, which can then be queried using
     * bestPath().
     */
    public void singleSourceDistances(Node source) {
        // Implementation constraint: use Dijkstra's single-source shortest paths algorithm.
        PQueue<Node> frontier = new PQueue<>() {
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
            public void add(Node node, double priority) throws IllegalArgumentException {

            }

            @Override
            public Node peek() {
                return null;
            }

            @Override
            public Node extractMin() {
                return null;
            }

            @Override
            public double getPriority(Node element) {
                return 0;
            }

            @Override
            public void changePriority(Node node, double p) {

            }
        };
        distances = new HashMap<>();
        bestEdges = new HashMap<>();
        coinDistances = new TreeMap<>();

        // First, we can add the source vertex to the frontier (0 distance to itself).
        frontier.add(source, 0);
        distances.put(source, 0.);

        // Loop until the entire graph is traversed and 'settled.'
        while (!frontier.isEmpty()) {
            // Start by taking out the smallest-distance vertex in the frontier.
            Node toAnalyze = frontier.extractMin();

            // Iterate over each neighboring edge from that vertex.
            for (Edge edge : graph.outgoingEdges(toAnalyze)) {
                // Grab the vertex on the other side of the edge.
                Node neighboringVertex = graph.dest(edge);

                // Calculate the total distance to this vertex through the source vertex.
                double distance = distances.get(toAnalyze) + graph.weight(edge);

                // Check whether a new distance or lesser distance has been found.
                if (!distances.containsKey(neighboringVertex) ||
                        distances.get(neighboringVertex) > distance) {
                    distances.put(neighboringVertex, distance);
                    bestEdges.put(neighboringVertex, edge);

                    // If the tile is a coin that has not yet been claimed, add it to the map of
                    // coin distances.
                    if (neighboringVertex.getTile().coins() > 0) {
                        if (!coinDistances.containsKey(distance)) {
                            TreeSet<Node> distList = new TreeSet<>(
                                    Comparator.comparingInt(n -> n.getTile().originalCoinValue()));
                            distList.add(neighboringVertex);
                            coinDistances.put(distance, distList);
                        } else {
                            coinDistances.get(distance).add(neighboringVertex);
                        }
                    }

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
    public double getDistance(Node v) {
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
    public List<Edge> bestPath(Node target) {
        assert !bestEdges.isEmpty() : "Must run singleSourceDistances() first";
        LinkedList<Edge> path = new LinkedList<>();
        Node v = target;
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

    /**
     * Returns: the closest coin to the source vertex. If there are multiple with the same distance,
     * it returns the first coin in the set.
     */
    public Node closestCoin() {
        if (coinDistances.isEmpty()) {
            return null;
        }
        return coinDistances.firstEntry().getValue().first();
    }

    /**
     * Returns the highest-value coin within `relDist` steps from the closest coin.
     */
    public Node greedyIn(double relDist) {
        TreeMap<Double, TreeSet<Node>> copy = new TreeMap<>();
        for (Entry<Double, TreeSet<Node>> coin : coinDistances.entrySet()) {
            copy.put(coin.getKey(), (TreeSet<Node>) coin.getValue().clone());
        }
        Entry<Double, TreeSet<Node>> minCoin = copy.pollFirstEntry();
        Entry<Double, TreeSet<Node>> nextCoin = minCoin;
        Node largestCoin = minCoin.getValue().first();
        while (nextCoin.getKey() - relDist < minCoin.getKey()) {
            Node temp = nextCoin.getValue().pollFirst();
            if (largestCoin.getTile().coins() < temp.getTile().coins()) {
                largestCoin = temp;
            }
            if (nextCoin.getValue().isEmpty()) {
                nextCoin = copy.pollFirstEntry();
                if (nextCoin == null) {
                    break;
                }
            }
        }
        return largestCoin;
    }
}

/**
 * A graph object that is used to represent the known tiles and the connections between them, where
 * each tile is represented with a long (for its id) and each edge is represented with an array
 * consisting of the source and destination id.
 */
class SeekGraph implements WeightedDigraph<Long, long[]> {

    /**
     * An array that holds every edge in the graph, consisting of the edge's source vertex and
     * destination vertex (represented by ids of type long).
     */
    long[][] edges;

    /**
     * A map that stores all the edges corresponding to each vertex in the graph.
     */
    Map<Long, Set<long[]>> outgoing;

    /**
     * The constructor for a SeekGraph object that initializes all fields and properly fills them
     * based on the array of edges provided.
     */
    SeekGraph(long[][] edges) {
        this.edges = edges;
        this.outgoing = new HashMap<>();
        for (long[] edge : edges) {
            if (!outgoing.containsKey(edge[0])) {
                outgoing.put(edge[0], new HashSet<>());
            }
            outgoing.get(edge[0]).add(edge);
            if (!outgoing.containsKey(edge[1])) {
                outgoing.put(edge[1], new HashSet<>());
            }
            outgoing.get(edge[1]).add(edge);
        }
    }

    /**
     * Adds a new edge to the graph with source vertex `from` to destination vertex `to`.
     */
    public void add(long from, long to) {
        if (!outgoing.containsKey(from)) {
            outgoing.put(from, new HashSet<>());
        }
        outgoing.get(from).add(new long[] {from, to});
        if (!outgoing.containsKey(to)) {
            outgoing.put(to, new HashSet<>());
        }
        outgoing.get(to).add(new long[] {to, from});
    }

    @Override
    public Iterable<long[]> outgoingEdges(Long vertex) {
        return outgoing.get(vertex);
    }

    @Override
    public Long source(long[] edge) {
        return edge[0];
    }

    @Override
    public Long dest(long[] edge) {
        return edge[1];
    }

    @Override
    public double weight(long[] edge) {
        return 1;
    }
}