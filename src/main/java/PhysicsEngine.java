import math.Vector2d;
import objects.Bubble;
import objects.Wall;

import java.awt.*;
import java.util.ArrayList;

class PhysicsEngine {

    private static final int NUM_INITIAL_BUBBLES = 10;
    private Wall[] walls;
    private ArrayList<Bubble> bubbles;

    public PhysicsEngine() {
        // Créer les murs.
        Vector2d v = new Vector2d(10, 400);
        Vector2d h = new Vector2d(600, 10);
        walls = new Wall[]{
                new Wall(v, new Vector2d(0, 0), false, 10, Color.blue),  // Left
                new Wall(h, new Vector2d(0, 0), true, 10, Color.blue),  // Top
                new Wall(v, new Vector2d(590, 0), false, 0, Color.blue),  // Right
                new Wall(h, new Vector2d(0, 390), true, 0, Color.blue) // Bottom
        };

        // Créer les bulles initiales.
        bubbles = new ArrayList<Bubble>();
        for (int i = 0; i < NUM_INITIAL_BUBBLES; i++) {
            double size = 5.0 + Math.random() * 20.0;
            Vector2d position = new Vector2d(
                    Math.random() * 200 + 50, Math.random() * 100 + 50);
            Vector2d speed = new Vector2d(
                    Math.random() * 0.2, Math.random() * 0.2);
            bubbles.add(new Bubble(size, position, speed));
        }
    }

    public ArrayList<Bubble> getBubbles() {
        return bubbles;
    }

    public Wall[] getWalls() {
        return walls;
    }

    public void update(double delta) {
        Bubble bubble;
        Bubble otherBubble;

        for (int i = 0; i < bubbles.size(); i++) {
            bubble = bubbles.get(i);
            Vector2d position = bubble.getPosition();
            Vector2d speed = bubble.getSpeed();
            bubble.setPosition(position.add(speed.multiply(delta)));
        }

        // Vérifier si la bulle intersecte une autre bulle.
        for (int i = 0; i < bubbles.size(); i++) {
            bubble = bubbles.get(i);
            for (int j = i + 1; j < bubbles.size(); j++) {
                otherBubble = bubbles.get(j);
                Vector2d intersection = bubble.intersects(otherBubble);
                if (intersection == null)
                    continue;
                bubble.collide(otherBubble, intersection);
            }
        }

        // Vérifier si la bulle intersecte un mur.
        for (int i = 0; i < bubbles.size(); i++) {
            bubble = bubbles.get(i);
            for (int j = 0; j < walls.length; j++)
                if (bubble.intersects(walls[j]))
                    bubble.collide(walls[j]);
        }
    }

}