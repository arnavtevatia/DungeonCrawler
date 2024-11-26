// Enemy.java
package game;
import diver.*;

public class Enemy {
    private int health;
    private int damage;
    private String name;

    // Constructor to create an enemy with health and damage
    public Enemy(String name, int health, int damage) {
        this.name = name;
        this.health = health;
        this.damage = damage;
    }

    // Getters and setters
    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void attack(Player player) {
        player.takeDamage(damage);
        System.out.println(name + " attacks! Player loses " + damage + " health.");
    }

    public String getName() {
        return name;
    }
}