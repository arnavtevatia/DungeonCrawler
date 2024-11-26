# Dungeon Crawler Game

## Project Overview
Dungeon Navigator is a dungeon exploration and navigation game that combines graph algorithms and GUI visualization to create an engaging gameplay experience. Players must find a ring in the maze during the "Seek" phase and then collect treasures and escape within a limited number of steps during the "Scram" phase. The system leverages custom implementations of Dijkstra's algorithm for pathfinding and prioritizes efficiency and interactivity through a dynamic graphical interface.

## Features
Graph-Based Pathfinding:

Implements Dijkstra's algorithm to determine the shortest and most efficient paths for both phases.
- Adapts the algorithm to maximize treasure collection during the "Scram" phase.
Treasure Collection and Escape:
- Efficiently prioritizes high-value treasures while ensuring the player escapes within the step limit.
Dynamic GUI Integration:
- Interactive maze visualization using MazePanel, with real-time path highlighting and updates.
- Visual cues for player movement, visited tiles, and collected treasures.

Custom Game Logic:
- Modular design with DungeonManager managing gameplay, Player for player-specific actions, and game states like SeekState and ScramState.

## Tools and Technologies
Java: Primary programming language for the game and algorithm implementations.

Swing: GUI framework for interactive visualization of the dungeon and player actions.

JUnit: For comprehensive unit testing of algorithms and gameplay logic.
## Installation
Prerequisites
- Ensure you have Java (JDK 11 or higher) installed.

Compiling and Running
1. Clone the repository:
git clone https://github.com/arnavtevatia/DungeonCrawler

2. Compile the project:
javac <main-class-path>.java

3. Run the game:
java <main-class-path>

## Limitations
Simplified Gameplay Logic: Currently focused on core pathfinding and treasure collection; additional enemy AI or dynamic obstacles could be added.

Performance: The system handles medium-sized dungeons efficiently but may face performance issues with significantly larger mazes.

Single Player: The current implementation supports only single-player gameplay.
## Usage
- Start the game with java game.Main.
- During the Seek Phase, find the ring using visual cues and efficient pathfinding.
- During the Scram Phase, collect as much treasure as possible and escape before running out of steps.
- Watch your progress and results in the GUI.
