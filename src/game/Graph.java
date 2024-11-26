// Graph.java (inside the game package)
package game;

import java.util.HashMap;
import java.util.Map;

public class Graph {
    // A map to store nodes, with each node's ID as the key
    private Map<Long, Node> nodes;

    // Constructor to initialize the graph
    public Graph() {
        nodes = new HashMap<>();
    }

    // Method to add a node (room) to the graph
    public void addNode(Node node) {
        nodes.put(node.getId(), node);
    }

    // Method to get a node by its ID
    public Node getNode(long id) {
        return nodes.get(id);
    }

    // Method to add an edge (path) between two nodes
    public void addEdge(Node from, Node to, int weight) {
        Edge edge = new Edge(from, to, weight);
        from.addEdge(edge); // Add the edge to the 'from' node
        to.addEdge(edge); // Add the edge to the 'to' node (undirected graph)
    }

    // Method to get all nodes in the graph
    public Map<Long, Node> getNodes() {
        return nodes;
    }
}