// DungeonManager.java (implements Navigator)
package diver;

import datastructures.HeapPQueue;
import game.*;
import gui.MazePanel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

public class DungeonManager implements Navigator {
    private Player player;
    private GameState gameState;
    private Graph dungeonGraph;

    private MazePanel mazePanel;

    private Tile getTileFromGraph(long nodeId) {
        Node node = dungeonGraph.getNode(nodeId);
        return node != null ? node.getTile() : null;
    }


    public DungeonManager(Player player, GameState gameState, MazePanel mazePanel) {
        this.player = player;
        this.gameState = gameState;
        this.dungeonGraph = new Graph();
        this.mazePanel = mazePanel; // Save the reference to the MazePanel
    }


    // Constructor for DungeonManager
    public DungeonManager(Player player, GameState gameState) {
        this.player = player;
        this.gameState = gameState;
        this.dungeonGraph = new Graph();
    }

    // Method to generate a dungeon with enemies
    @Override
    /*
    public void generateDungeon() {
        Tile startTile = new Tile(0, 0, 0, Tile.TileType.ENTRANCE);
        Tile treasureTile = new Tile(1, 0, 100, Tile.TileType.RING);
        Tile exitTile = new Tile(2, 0, 0, Tile.TileType.ENTRANCE);

        // Create nodes for the dungeon
        Node startNode = new Node(0L, startTile);
        Node treasureRoom = new Node(1L, treasureTile);
        Node exitNode = new Node(2L, exitTile);

        dungeonGraph.addNode(startNode);
        dungeonGraph.addNode(treasureRoom);
        dungeonGraph.addNode(exitNode);

        // Connect the rooms (nodes) with edges (paths)
        dungeonGraph.addEdge(startNode, treasureRoom, 10); // Start -> Treasure Room
        dungeonGraph.addEdge(treasureRoom, exitNode, 5); // Treasure Room -> Exit

        System.out.println("Dungeon generated!");
    }

     */
    public void generateDungeon() {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            Tile tile = new Tile(i, rand.nextInt(3), rand.nextInt(100), Tile.TileType.values()[rand.nextInt(Tile.TileType.values().length)]);
            Node node = new Node((long) i, tile);
            dungeonGraph.addNode(node);
            if (i > 0) {
                dungeonGraph.addEdge(dungeonGraph.getNode((long) (i - 1)), node, rand.nextInt(10) + 1);
            }
        }
        System.out.println("Randomized dungeon generated!");
    }


    // Method to start the game
    @Override
    public void startGame() {
        generateDungeon();  // Generate dungeon layout
        System.out.println("Game started!");
        gameLoop();  // Start the game loop
    }

    // Main game loop
    @Override
    public void gameLoop() {
        while (player.isAlive() && player.canMove()) {
            System.out.println("Player Location: " + player.currentLocation());
            System.out.println("Player Health: " + player.getHealth());
            System.out.println("Inventory: " + player.getInventory());

            // Handle player movement
            movePlayer();

            // Check for enemy encounter in the current room
            Enemy enemy = checkForEnemy((int) player.currentLocation());
            if (enemy != null) {
                System.out.println("Encountered " + enemy.getName() + "!");
                combat(player, enemy);  // Start combat if an enemy is present
            }

            // Example: Check if player reached the exit
            if (player.currentLocation() == 2) {
                System.out.println("Player has reached the exit!");
                break;
            }
        }

        if (!player.isAlive()) {
            System.out.println("Game Over! Player is dead.");
        }
    }

    // Check for enemy in the current room (simplified example)
    private Enemy checkForEnemy(int currentLocation) {
        // Example: Check if the player is in a room with an enemy (just a simple check)
        if (currentLocation == 1) {  // Assume room 1 has an enemy
            return new Enemy("Goblin", 50, 10);  // Example enemy
        }
        return null;  // No enemy in the current room
    }

    // Combat method to handle player vs. enemy interactions
    private void combat(Player player, Enemy enemy) {
        while (player.isAlive() && enemy.isAlive()) {
            // Player attacks first
            System.out.println("Player attacks!");
            enemy.takeDamage(10);  // Example: Player deals 10 damage per attack
            System.out.println("Enemy health is now " + enemy.getHealth());

            // If enemy is still alive, it attacks back
            if (enemy.isAlive()) {
                enemy.attack(player);  // Enemy attacks player
                System.out.println("Player health is now " + player.getHealth());
            }
        }

        // Outcome of combat
        if (player.isAlive()) {
            System.out.println("Player defeated the " + enemy.getName() + "!");
            player.collectItem("Defeated " + enemy.getName());
        } else {
            System.out.println("Player was defeated by " + enemy.getName() + "!");
        }
    }

    // Method to handle player movement (example)
    @Override
    public void movePlayer() {
        // For simplicity, we're just moving the player to the next tile (room)
        // In a full game, you'd want to add user input or pathfinding logic here
        if (player.canMove()) {
            player.moveTo(1);  // Moving player to the next room (e.g., Treasure Room)
        }
    }

    // Example combat method (could be more complex with enemies)
    @Override
    public void combat() {
        // If player is on an enemy's tile, combat occurs
        // Example: player loses health upon encountering an enemy
        if (player.currentLocation() == 1) { // e.g., player enters the "Treasure Room" and encounters an enemy
            player.takeDamage(10);  // Damage player by 10 HP (for example)
            System.out.println("Combat occurred! Player lost 10 health.");
        }
    }

    // Update the game state after each action (could involve inventory management, level progression, etc.)
    @Override
    public void updateGameState() {
        // This would update the game state based on player actions (e.g., collecting treasure, combat, etc.)
        System.out.println("Game state updated.");
    }

    // Implementing the seek method required by Navigator interface
    @Override
    public void seek(SeekState seekState) {
        Map<Long, Integer> gScore = new HashMap<>();
        Map<Long, Long> cameFrom = new HashMap<>();
        PriorityQueue<Long> openSet = new PriorityQueue<>(
                Comparator.comparingInt(node -> gScore.getOrDefault(node, Integer.MAX_VALUE) + heuristic(node, seekState))
        );

        long start = seekState.currentLocation();
        gScore.put(start, 0);
        openSet.add(start);

        List<Tile> pathToHighlight = new ArrayList<>();

        while (!openSet.isEmpty()) {
            long current = openSet.poll();

            if (seekState.distanceToRing() == 0) {
                pathToHighlight = reconstructPathToTiles(seekState, cameFrom, current);
                mazePanel.highlightAndMovePlayer(pathToHighlight);
                System.out.println("Player has found the ring!");
                return;
            }

            for (NodeStatus neighbor : seekState.neighbors()) {
                long neighborId = neighbor.getId();
                int tentativeGScore = gScore.getOrDefault(current, Integer.MAX_VALUE) + 1;

                if (tentativeGScore < gScore.getOrDefault(neighborId, Integer.MAX_VALUE)) {
                    cameFrom.put(neighborId, current);
                    gScore.put(neighborId, tentativeGScore);
                    openSet.add(neighborId);
                }
            }
        }

        System.out.println("Failed to find the ring.");
    }

    private List<Tile> reconstructPathToTiles(SeekState seekState, Map<Long, Long> cameFrom, long current) {
        List<Tile> path = new ArrayList<>();
        while (cameFrom.containsKey(current)) {
            Tile tile = getTileFromGraph(current); // Get tile from the graph
            if (tile != null) {
                path.add(0, tile);
            }
            current = cameFrom.get(current);
        }
        return path;
    }




    private void reconstructPath(SeekState seekState, Map<Long, Long> cameFrom, long current) {
        while (cameFrom.containsKey(current)) {
            seekState.moveTo(current);
            current = cameFrom.get(current);
        }
    }

    private List<Node> reconstructPath(Map<Node, Node> cameFrom, Node current) {
        List<Node> path = new ArrayList<>();
        while (cameFrom.containsKey(current)) {
            path.add(0, current);
            current = cameFrom.get(current);
        }
        return path;
    }

    private int heuristic(long node, SeekState seekState) {
        // Use the distanceToRing as the heuristic
        return seekState.distanceToRing();
    }

    // Implementing the scram method required by Navigator interface
    @Override
    public void scram(ScramState scramState) {
        Node current = scramState.currentNode();
        Node exit = scramState.exit();
        int stepsRemaining = scramState.stepsToGo();

        PriorityQueue<Node> treasureQueue = new PriorityQueue<>(
                Comparator.comparingDouble(node -> -node.getTile().coins() / shortestPath(scramState, node).size())
        );

        for (Node node : scramState.allNodes()) {
            if (node.getTile().coins() > 0) {
                treasureQueue.add(node);
            }
        }

        while (stepsRemaining > 0 && !current.equals(exit)) {
            if (!treasureQueue.isEmpty()) {
                Node nextTreasure = treasureQueue.poll();
                List<Node> pathToTreasure = shortestPath(scramState, nextTreasure);

                // Highlight path to treasure
                List<Tile> tilePath = convertNodesToTiles(pathToTreasure, scramState);
                mazePanel.highlightAndMovePlayer(tilePath);

                for (Node step : pathToTreasure) {
                    scramState.moveTo(step);
                    stepsRemaining--;
                    current = step;
                    if (stepsRemaining <= 0) break;
                }
                continue;
            }

            // Move to the exit
            List<Node> pathToExit = shortestPath(scramState, exit);
            List<Tile> tilePath = convertNodesToTiles(pathToExit, scramState);
            mazePanel.highlightAndMovePlayer(tilePath);

            for (Node step : pathToExit) {
                scramState.moveTo(step);
                stepsRemaining--;
                current = step;
                if (stepsRemaining <= 0) break;
            }
        }

        if (!current.equals(exit)) {
            System.out.println("Failed to escape in time!");
        } else {
            System.out.println("Successfully escaped with treasures!");
        }
    }

    private List<Tile> convertNodesToTiles(List<Node> nodes, ScramState scramState) {
        List<Tile> tiles = new ArrayList<>();
        for (Node node : nodes) {
            tiles.add(scramState.getTile(node));
        }
        return tiles;
    }


    private int heuristic(long currentLocation, long targetLocation) {
        int currentRow = (int) (currentLocation >> 32);
        int currentCol = (int) currentLocation;
        int targetRow = (int) (targetLocation >> 32);
        int targetCol = (int) targetLocation;
        return Math.abs(currentRow - targetRow) + Math.abs(currentCol - targetCol);
    }

    private List<Node> shortestPath(ScramState state, Node target) {
        Map<Node, Integer> distances = new HashMap<>();
        Map<Node, Node> cameFrom = new HashMap<>();
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        // Initialize distances and add starting node
        for (Node node : state.allNodes()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        Node start = state.currentNode();
        distances.put(start, 0);
        openSet.add(start);

        // Dijkstra's algorithm
        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (current.equals(target)) {
                return reconstructPath(cameFrom, current);
            }

            for (Node neighbor : current.getNeighbors()) { // Replace with correct method for getting neighbors
                int tentativeDistance = distances.get(current) + edgeWeight(current, neighbor);
                if (tentativeDistance < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    distances.put(neighbor, tentativeDistance);
                    cameFrom.put(neighbor, current);
                    openSet.add(neighbor);
                }
            }
        }

        return Collections.emptyList(); // Return empty path if target is unreachable
    }

    private int edgeWeight(Node from, Node to) {
        // Replace with appropriate logic for calculating edge weights
        // Example: Return 1 for uniform weights or a custom value for dynamic costs
        return 1;
    }


}