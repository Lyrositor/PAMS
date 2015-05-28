package org.pcc.pams;

import org.pcc.pams.math.Vector2d;
import org.pcc.pams.objects.Bubble;
import org.pcc.pams.objects.Fan;
import org.pcc.pams.objects.Wall;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Performs all physics simulations on the bubbles.
 * <p>
 * Note: the {@code bubbles} list must remain synchronized across various
 * threads, and should always be surrounded in a synchronized block when this
 * class is modified.
 */
class PhysicsEngine {

    /**
     * The initial number of bubbles to generate.
     */
    public static final int NUM_INITIAL_BUBBLES = 3;
    /**
     * The fan optionally used to blow bubbles in a certain direction.
     */
    private final Fan fan;
    /**
     * A list of currently-simulated bubbles.
     * <p>
     * Must be kept synchronized across every thread.
     */
    private final List<Bubble> bubbles;
    /**
     * An array of four walls delimiting the simulation.
     */
    private final Wall[] walls;
    /**
     * A list of listeners to notify when a physical event occurs.
     */
    private final ArrayList<PhysicsListener> listeners = new ArrayList<>();

    /**
     * Initialize the physics engine with four walls and a few bubbles.
     *
     * @param dim The dimensions of the simulation frame.
     */
    public PhysicsEngine(int[] dim) {
        // Create the fan.
        fan = new Fan(new Vector2d(dim[0] / 2, dim[1] / 2), Color.CYAN);

        // Create the walls.
        // Walls are stored as an array in this order: Left, Top, Right, Bottom.
        Vector2d v = new Vector2d(10, dim[1] - 20);
        Vector2d h = new Vector2d(dim[0], 10);
        Color color = Color.YELLOW;
        walls = new Wall[] {
                new Wall(v, new Vector2d(0, 10), false, 10, color),
                new Wall(h, new Vector2d(0, 0), true, 10, color),
                new Wall(v, new Vector2d(dim[0] - 10, 10), false, 0, color),
                new Wall(h, new Vector2d(0, dim[1] - 10), true, 0, color)
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
            double x = walls[2].getBoundsLine().x - walls[0].getBoundsLine().x;
            double y = walls[3].getBoundsLine().y - walls[1].getBoundsLine().y;
            for (int i = 0; i < count; i++) {
                double radius = Bubble.MAX_RADIUS * (0.8 * Math.random() + 0.2);
                Vector2d position;
                do {
                    position = new Vector2d(
                            radius + Math.random() * (x - 2 * radius),
                            radius + Math.random() * (y - 2 * radius));
                } while (!isEmptySpace(position, radius));
                Vector2d speed = new Vector2d(
                        0.2 + 0.8 * Math.random(), 0.2 + 0.8 * Math.random()
                ).product(Bubble.MAX_SPEED);
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
                bubble.setSpeed(
                        speed.getNormed(Math.max(1, speed.norm() + delta)));
            }
        }
    }

    /**
     * Adds a {@link org.pcc.pams.PhysicsListener} to notify when a physical
     * event occurs.
     *
     * @param newListener The {@link org.pcc.pams.PhysicsListener} to add.
     */
    public void addListener(PhysicsListener newListener) {
        listeners.add(newListener);
    }

    /**
     * Returns the fan being used.
     *
     * @return The fan in use.
     */
    public Fan getFan() {
        return fan;
    }

    /**
     * Returns a synchronized list of current bubbles.
     *
     * @return A dynamic list of bubbles in the simulation.
     */
    public List<Bubble> getBubbles() {
        synchronized (bubbles) {
            return bubbles;
        }
    }

    /**
     * Counts the number of bubbles and returns the result.
     *
     * @return The number of bubbles in the simulation.
     */
    public int getBubbleCount() {
        synchronized (bubbles) {
            return bubbles.size();
        }
    }

    /**
     * Returns the four walls representing the boundaries of the simulation.
     *
     * @return An array of four walls.
     */
    public Wall[] getWalls() {
        return walls;
    }

    /**
     * Updates the simulation by the specified amount of time.
     *
     * @param delta The time step to use for updating.
     */
    public void update(double delta) {
        Bubble bubble;
        Bubble otherBubble;
        ListIterator<Bubble> i;

        synchronized (bubbles) {
            // Lower the walls' collision intensity.
            for (Wall wall : walls)
                wall.adjustCollisionIntensity(Wall.INTENSITY_DEC * delta);

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
                                l.bubbleToWallCollision(bubble, wall);
                            wall.adjustCollisionIntensity(
                                    Wall.INTENSITY_INC * delta);
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
        synchronized (bubbles) {
            for (Bubble b : bubbles)
                if (b.intersects(center, radius))
                    return false;
        }
        return true;
    }

    /**
     * Calculates and returns the total amount of the bubbles' kinetic energy.
     *
     * @return The total amount of kinetic energy (unit-less).
     */
    public double getTotalKineticEnergy() {
        double energy = 0;
        synchronized (bubbles) {
            for (Bubble bubble : bubbles)
                energy += 0.5 * bubble.getMass() * bubble.getSpeed().norm2();
        }
        return energy;
    }
}