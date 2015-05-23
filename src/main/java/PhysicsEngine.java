import math.Vector2d;
import objects.Bubble;
import objects.Fan;
import objects.Wall;

import java.awt.*;
import java.util.*;
import java.util.List;

class PhysicsEngine {

    public static final int NUM_INITIAL_BUBBLES = 3;

    private final Fan fan;
    private final List<Bubble> bubbles;
    private final Wall[] walls;
    private final ArrayList<PhysicsListener> listeners = new ArrayList<>();

    /**
     * Initialize the physics engine with four walls and a few bubbles.
     *
     * @param dim The dimensions of the simulation frame.
     */
    public PhysicsEngine(int[] dim) {
        // Create the fan.
        fan = new Fan(new Vector2d(dim[0] / 2, dim[1] / 2), Color.cyan);

        // Create the walls.
        Vector2d v = new Vector2d(10, dim[1]);
        Vector2d h = new Vector2d(dim[0], 10);
        walls = new Wall[]{
                new Wall(v, new Vector2d(0, 0), false, 10, Color.blue),  // Left
                new Wall(h, new Vector2d(0, 0), true, 10, Color.blue),  // Top
                new Wall(v, new Vector2d(dim[0] - 10, 0), false, 0, Color.blue),  // Right
                new Wall(h, new Vector2d(0, dim[1] - 10), true, 0, Color.blue) // Bottom
        };

        // Create the initial bubbles.
        bubbles = Collections.synchronizedList(new ArrayList<Bubble>());
        addBubbles(NUM_INITIAL_BUBBLES);
    }

    /**
     * Adds a number of bubbles to the simulation.
     *
     * @param count The amount of bubbles to add.
     */
    public void addBubbles(int count) {
        synchronized (bubbles) {
            double width = walls[2].getBoundsLine().x - walls[0].getBoundsLine().x;
            double height = walls[1].getBoundsLine().y - walls[3].getBoundsLine().y;
            for (int i = 0; i < count; i++) {
                double radius = Bubble.MAX_RADIUS * (0.8 * Math.random() + 0.2);
                Vector2d position;
                do {
                    position = new Vector2d(
                            radius + Math.random() * (width - 2 * radius),
                            -(radius + Math.random() * (height - 2 * radius)));
                } while (!isEmptySpace(position, radius));
                Vector2d speed = new Vector2d(
                        Math.random(), Math.random()).product(Bubble.MAX_SPEED);
                bubbles.add(new Bubble(radius, position, speed));
            }
        }
    }

    /**
     * Removes a number of bubbles from the simulation.
     *
     * The bubbles are removed in the order in which they were added (FIFO).
     * @param count The amount of bubbles to remove.
     */
    public void removeBubbles(int count) {
        synchronized (bubbles) {
            ListIterator<Bubble> i = bubbles.listIterator();
            int num = 0;
            while (i.hasNext() && num < count) {
                i.next();
                i.remove();
                num++;
            }
        }
    }

    /**
     * Adjusts the speed of all bubbles.
     *
     * @param delta The variation of speed to apply (to the norm).
     */
    public void adjustSpeed(double delta) {
        synchronized (bubbles) {
            for (Bubble bubble : bubbles) {
                Vector2d speed = bubble.getSpeed();
                bubble.setSpeed(speed.getNormed(Math.max(1, speed.norm() + delta)));
            }
        }
    }

    /**
     * Adds a PhysicsListener to notify when a physical event occurs.
     *
     * @param newListener The PhysicsListener to add.
     */
    public void addListener(PhysicsListener newListener) {
        listeners.add(newListener);
    }

    public Fan getFan() {
        return fan;
    }

    public List<Bubble> getBubbles() {
        return bubbles;
    }

    public int getBubbleCount() {
        return bubbles.size();
    }

    public Wall[] getWalls() {
        return walls;
    }

    public void update(double delta) {
        Bubble bubble;
        Bubble otherBubble;
        ListIterator<Bubble> i;

        synchronized (bubbles) {
            // Update the bubbles' position.
            i = bubbles.listIterator();
            while (i.hasNext()) {
                bubble = i.next();

                // Calculate the new speed, as affected by the wind.
                Vector2d speed = bubble.getSpeed();
                if (bubble.intersects(fan)) {
                    Vector2d diff = bubble.getPosition().sub(fan.getPosition());
                    Vector2d force = diff.getNormed(fan.getIntensity());
                    Vector2d newSpeed = bubble.getSpeed().sum(new Vector2d(
                            delta * force.x,
                            delta * force.y
                    ));
                    bubble.setSpeed(newSpeed);
                }

                // Calculate the new position.
                Vector2d position = bubble.getPosition();
                bubble.setPosition(position.sum(speed.product(delta)));
            }

            //Check if any bubbles intersect with other bubbles.
            i = bubbles.listIterator();
            while (i.hasNext()) {
                bubble = i.next();
                Iterator<Bubble> j = bubbles.listIterator(i.nextIndex());
                while (j.hasNext()) {
                    otherBubble = j.next();
                    if (bubble.intersects(otherBubble)) {
                        Vector2d oldSpeed = bubble.getSpeed();
                        bubble.collide(otherBubble);
                        if (!oldSpeed.equals(bubble.getSpeed())) {
                            for (PhysicsListener l : listeners)
                                l.bubbleToBubbleCollision(bubble, otherBubble);
                        }
                    }
                }
            }

            // Check if any bubbles interact with walls.
            i = bubbles.listIterator();
            while (i.hasNext()) {
                bubble = i.next();
                for (Wall wall : walls)
                    if (bubble.intersects(wall)) {
                        Vector2d oldSpeed = bubble.getSpeed();
                        bubble.collide(wall);
                        if (!oldSpeed.equals(bubble.getSpeed())) {
                            for (PhysicsListener l : listeners)
                                l.bubbleToWallCollision(bubble);
                        }
                    }
            }
        }
    }

    /**
     * Checks if the specified circular area is empty.
     *
     * @param center The center of the circle.
     * @param radius The circle's radius.
     * @return True if the space is empty, false otherwise.
     */
    private boolean isEmptySpace(Vector2d center, double radius) {
        for (Bubble b : bubbles)
            if (b.intersects(center, radius))
                return false;
        return true;
    }

}