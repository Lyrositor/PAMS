import math.Vector2d;
import objects.Bubble;
import objects.Wall;

import java.awt.*;
import java.util.*;
import java.util.List;

class PhysicsEngine {

    private static final int NUM_INITIAL_BUBBLES = 1;
    private Wall[] walls;
    private List bubbles;
    private ArrayList<PhysicsListener> listeners = new ArrayList<PhysicsListener>();

    public PhysicsEngine(int[] dim) {
        // Cr�er les murs.
        Vector2d v = new Vector2d(10, dim[1]);
        Vector2d h = new Vector2d(dim[0], 10);
        walls = new Wall[]{
                new Wall(v, new Vector2d(0, 0), false, 10, Color.blue),  // Left
                new Wall(h, new Vector2d(0, 0), true, 10, Color.blue),  // Top
                new Wall(v, new Vector2d(dim[0] - 10, 0), false, 0, Color.blue),  // Right
                new Wall(h, new Vector2d(0, dim[1] - 10), true, 0, Color.blue) // Bottom
        };

        // Cr�er les bulles initiales.
        bubbles = Collections.synchronizedList(new ArrayList<Bubble>());
        addBubbles(NUM_INITIAL_BUBBLES);
    }

    public void addBubbles(int count) {
        synchronized (bubbles) {
            for (int i = 0; i < count; i++) {
                double size = 5.0 + Math.random() * 20.0;
                Vector2d position = new Vector2d(
                        Math.random() * 200 + 50, Math.random() * 100 + 50);
                Vector2d speed = new Vector2d(
                        Math.random() * 0.2, Math.random() * 0.2);
                bubbles.add(new Bubble(size, position, speed));
            }
        }
    }

    public void removeBubbles(int count) {
        int lastIndex = bubbles.size() - count - 1;
        synchronized (bubbles) {
            ListIterator<Bubble> i = bubbles.listIterator(bubbles.size() - 1);
            while (i.hasNext() && i.nextIndex() > lastIndex) {
                i.next();
                i.remove();
            }
        }
    }

    public void addListener(PhysicsListener newListener) {
        listeners.add(newListener);
    }

    public List<Bubble> getBubbles() {
        return bubbles;
    }

    public Wall[] getWalls() {
        return walls;
    }

    public void update(double delta) {
        Bubble bubble;
        Bubble otherBubble;
        ListIterator<Bubble> i;

        synchronized (bubbles) {
            // Mettre � jour la position des bubbles.
            i = bubbles.listIterator();
            while (i.hasNext()) {
                bubble = i.next();
                Vector2d position = bubble.getPosition();
                Vector2d speed = bubble.getSpeed();
                bubble.setPosition(position.add(speed.multiply(delta)));
            }

            // V�rifier si la bulle intersecte une autre bulle.
            i = bubbles.listIterator();
            while (i.hasNext()) {
                bubble = i.next();
                Iterator<Bubble> j = bubbles.listIterator(i.nextIndex());
                while (j.hasNext()) {
                    otherBubble = j.next();
                    Vector2d intersection = bubble.intersects(otherBubble);
                    if (intersection != null) {
                        Vector2d oldSpeed = bubble.getSpeed();
                        bubble.collide(otherBubble, intersection);
                        if (!oldSpeed.equals(bubble.getSpeed()))
                            for (PhysicsListener listener : listeners)
                                listener.bubbleToBubbleCollision();
                    }
                }
            }

            // V�rifier si la bulle entre en intersection avec un mur.
            i = bubbles.listIterator();
            while (i.hasNext()) {
                bubble = i.next();
                for (int j = 0; j < walls.length; j++)
                    if (bubble.intersects(walls[j])) {
                        Vector2d oldSpeed = bubble.getSpeed();
                        bubble.collide(walls[j]);
                        if (!oldSpeed.equals(bubble.getSpeed()))
                            for (PhysicsListener listener : listeners)
                                listener.bubbleToWallCollision();
                    }
            }
        }
    }

}