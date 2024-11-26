package graph;

import static org.junit.jupiter.api.Assertions.assertEquals;

import diver.DungeonManager;
import game.SeekState;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class ShortestPathsTest {

    static final String[] vertices1 = { "a", "b", "c", "d", "e", "f", "g" };
    static final int[][] edges1 = {
        {0, 1, 9}, {0, 2, 14}, {0, 3, 15},
        {1, 4, 23},
        {2, 4, 17}, {2, 3, 5}, {2, 5, 30},
        {3, 5, 20}, {3, 6, 37},
        {4, 5, 3}, {4, 6, 20},
        {5, 6, 16}
    };
    static class TestGraph implements WeightedDigraph<String, int[]> {
        int[][] edges;
        String[] vertices;
        Map<String, Set<int[]>> outgoing;

        TestGraph(String[] vertices, int[][] edges) {
            this.vertices = vertices;
            this.edges = edges;
            this.outgoing = new HashMap<>();
            for (String v : vertices) {
                outgoing.put(v, new HashSet<>());
            }
            for (int[] edge : edges) {
                outgoing.get(vertices[edge[0]]).add(edge);
            }
        }
        public Iterable<int[]> outgoingEdges(String vertex) { return outgoing.get(vertex); }
        public String source(int[] edge) { return vertices[edge[0]]; }
        public String dest(int[] edge) { return vertices[edge[1]]; }
        public double weight(int[] edge) { return edge[2]; }
    }
    static TestGraph testGraph1() {
        return new TestGraph(vertices1, edges1);
    }

    @Test
    void lectureNotesTest() {
        TestGraph graph = testGraph1();
        ShortestPaths<String, int[]> ssp = new ShortestPaths<>(graph);
        ssp.singleSourceDistances("a");
        assertEquals(50, ssp.getDistance("g"));
        StringBuilder sb = new StringBuilder();
        sb.append("best path:");
        for (int[] e : ssp.bestPath("g")) {
            sb.append(" " + vertices1[e[0]]);
        }
        sb.append(" g");
        assertEquals("best path: a c e f g", sb.toString());
    }

    @Test
    void berkeleyTest() {
        int[][] edges = {
                {0, 1, 10}, {0, 3, 30}, {0, 4, 100},
                {1, 2, 50},
                {2, 4, 10},
                {3, 2, 20}, {3, 4, 60}
        };
        String[] vertices = new String[] {"0", "1", "2", "3", "4"};
        TestGraph graph = new TestGraph(vertices, edges);
        ShortestPaths<String, int[]> ssp = new ShortestPaths<>(graph);
        ssp.singleSourceDistances("0");
        assertEquals(60, ssp.getDistance("4"));
        StringBuilder sb = new StringBuilder();
        sb.append("best path:");
        for (int[] e : ssp.bestPath("4")) {
            sb.append(" " + vertices[e[0]]);
        }
        sb.append(" 4");
        assertEquals("best path: 0 3 2 4", sb.toString());

    }

    @Test
    void washingtonTest() {
        int[][] edges = {
                {0, 1, 1}, {0, 2, 3}, {0, 5, 10},
                {1, 0, 1}, {1, 2, 1}, {1, 3, 7}, {1, 4, 5}, {1, 6, 2},
                {2, 0, 3}, {2, 1, 1}, {2, 3, 9}, {2, 4, 3},
                {3, 1, 7}, {3, 2, 9}, {3, 4, 2}, {3, 5, 1}, {3, 6, 12},
                {4, 1, 5}, {4, 2, 3}, {4, 3, 2}, {4, 5, 2},
                {5, 0, 10}, {5, 3, 1}, {5, 4, 2},
                {6, 1, 2}, {6, 3, 12}
        };
        String[] vertices = new String[] {"A", "B", "C", "D", "E", "F", "G"};
        TestGraph graph = new TestGraph(vertices, edges);
        ShortestPaths<String, int[]> ssp = new ShortestPaths<>(graph);
        ssp.singleSourceDistances("A");
        assertEquals(7, ssp.getDistance("F"));
        StringBuilder sb = new StringBuilder();
        sb.append("best path:");
        for (int[] e : ssp.bestPath("F")) {
            sb.append(" " + vertices[e[0]]);
        }
        sb.append(" F");
        assertEquals("best path: A B C E F", sb.toString());
    }

    @Test
    void cambridgeTest() {
        int[][] edges = {
                {0, 1, 3}, {0, 2, 4}, {0, 3, 5},
                {1, 0, 3}, {1, 2, 2}, {1, 5, 5},
                {2, 0, 4}, {2, 1, 2}, {2, 3, 7}, {2, 4, 3}, {2, 5, 6}, {2, 6, 2},
                {3, 0, 5}, {3, 2, 7}, {3, 4, 2},
                {4, 2, 3}, {4, 3, 2}, {4, 6, 4},
                {5, 1, 5}, {5, 2, 6}, {5, 6, 5}, {5, 7, 2},
                {6, 2, 2}, {6, 4, 4}, {6, 5, 5}, {6, 7, 1},
                {7, 5, 2}, {7, 6, 1}
        };
        String[] vertices = new String[] {"P", "Q", "R", "S", "T", "U", "V", "W"};
        TestGraph graph = new TestGraph(vertices, edges);
        ShortestPaths<String, int[]> ssp = new ShortestPaths<>(graph);
        ssp.singleSourceDistances("P");
        assertEquals(7, ssp.getDistance("W"));
        StringBuilder sb = new StringBuilder();
        sb.append("best path:");
        for (int[] e : ssp.bestPath("W")) {
            sb.append(" " + vertices[e[0]]);
        }
        sb.append(" W");
        assertEquals("best path: P R V W", sb.toString());
    }
}
