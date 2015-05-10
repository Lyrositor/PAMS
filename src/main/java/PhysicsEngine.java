import math.Vector2d;
import objects.Bubble;
import objects.Wall;

import java.awt.*;
import java.util.ArrayList;

class PhysicsEngine {

    private static final int NUM_INITIAL_BUBBLES = 1;
    private Wall[] walls;
    private ArrayList<Bubble> bubbles;

    public PhysicsEngine() {
        // Créer les murs.
        Vector2d v = new Vector2d(10, 200);
        Vector2d h = new Vector2d(300, 10);
        walls = new Wall[]{
                new Wall(v, new Vector2d(0, 0), false, 10, Color.blue),  // Left
                new Wall(h, new Vector2d(0, 0), true, 10, Color.blue),  // Top
                new Wall(v, new Vector2d(290, 0), false, 0, Color.blue),  // Right
                new Wall(h, new Vector2d(0, 190), true, 0, Color.blue) // Bottom
        };

        // Créer les bulles initiales.
        bubbles = new ArrayList<Bubble>();
        for (int i = 0; i < NUM_INITIAL_BUBBLES; i++)
            bubbles.add(new Bubble(25.0, new Vector2d(100, 100), new Vector2d(0.05, 0.05)));
    }

    public ArrayList<Bubble> getBubbles() {
        return bubbles;
    }

    public Wall[] getWalls() {
        return walls;
    }

    public void update(double delta) {
        Bubble bubble;

        for (int i = 0; i < bubbles.size(); i++) {
            bubble = bubbles.get(i);
            Vector2d position = bubble.getPosition();
            Vector2d speed = bubble.getSpeed();
            bubble.setPosition(position.add(speed.multiply(delta)));
        }

        // Vérifier si la bulle intersecte une autre bulle.
        for (int i = 0; i < bubbles.size(); i++) {
            bubble = bubbles.get(i);
            for (int j = 0; j < bubbles.size(); j++) {
                if (i == j)
                    continue;
                Vector2d intersection = bubble.intersects(bubbles.get(j));
                if (intersection == null)
                    continue;
                //http://en.wikipedia.org/wiki/Elastic_collision
            }
        }

        // Vérifier si la bulle intersecte un mur.
        for (int i = 0; i < bubbles.size(); i++) {
            bubble = bubbles.get(i);
            for (int j = 0; j < walls.length; j++) {
                Wall wall = walls[j];
                if (!bubble.intersects(wall))
                    continue;

                Vector2d newSpeed = bubble.getSpeed();
                Vector2d linePos = wall.getBoundsLine();
                Vector2d bubblePos = bubble.getPosition();
                if (wall.isHorizontal()) {
                    double sign = Math.signum(linePos.y - bubblePos.y);
                    newSpeed.y = -sign * Math.abs(newSpeed.y);
                } else {
                    double sign = Math.signum(linePos.x - bubblePos.x);
                    newSpeed.x = -sign * Math.abs(newSpeed.x);
                }
                bubble.setSpeed(newSpeed);
            }
        }
    }

}