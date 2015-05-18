import math.Vector2d;
import objects.Bubble;
import objects.Wall;

import java.awt.*;
import java.util.*;
import java.util.List;

class PhysicsEngine {

    private static final int NUM_INITIAL_BUBBLES = 1;
    private Wall[] walls;
    private List<Bubble> bubbles;
    private ArrayList<PhysicsListener> listeners = new ArrayList<>();

    public PhysicsEngine(int[] dim) {
        // Créer les murs.
        Vector2d v = new Vector2d(10, dim[1]);
        Vector2d h = new Vector2d(dim[0], 10);
        walls = new Wall[]{
                new Wall(v, new Vector2d(0, 0), false, 10, Color.blue),  // Left
                new Wall(h, new Vector2d(0, 0), true, 10, Color.blue),  // Top
                new Wall(v, new Vector2d(dim[0] - 10, 0), false, 0, Color.blue),  // Right
                new Wall(h, new Vector2d(0, dim[1] - 10), true, 0, Color.blue) // Bottom
        };

        // Créer les bulles initiales.
        bubbles = Collections.synchronizedList(new ArrayList<>());
        addBubbles(NUM_INITIAL_BUBBLES);
    }

    /**
     * Ajoute un certain nombre de bulles à la simulation.
     *
     * @param count Le nombre de bulles à ajouter.
     */
    public void addBubbles(int count) {
        synchronized (bubbles) {
            for (int i = 0; i < count; i++) {
                double size = Bubble.MAX_RADIUS * (0.8 * Math.random() + 0.2);
                Vector2d position = new Vector2d(
                        Math.random() * 200 + 50, Math.random() * 100 + 50);
                Vector2d speed = new Vector2d(
                        Math.random(), Math.random()).multiply(500);
                bubbles.add(new Bubble(size, position, speed));
            }
        }
    }

    /**
     * Enlève un certain nombre de bubbles à la simulation.
     * @param count Le nombre de bubbles à enlever.
     */
    public void removeBubbles(int count) {
        if (bubbles.size() == 0)
            return;
        int lastIndex = bubbles.size() - count - 1;
        synchronized (bubbles) {
            ListIterator<Bubble> i = bubbles.listIterator(bubbles.size() - 1);
            while (i.hasNext() && i.nextIndex() > lastIndex) {
                i.next();
                i.remove();
            }
        }
    }

    /**
     * Ajuste la vitesse de toutes les bulles.
     * @param delta La variation de vitesse à appliquer (en norme).
     */
    public void adjustSpeed(double delta) {
        synchronized (bubbles) {
            ListIterator<Bubble> i = bubbles.listIterator();
            while (i.hasNext()) {
                Bubble bubble = i.next();
                Vector2d speed = bubble.getSpeed();
                bubble.setSpeed(speed.setNorm(Math.max(1, speed.norm() + delta)));
            }
        }
    }

    /**
     * Ajoute un PhysicsListener à signaler lors d'un événement physique.
     * @param newListener Le PhysicsListener à ajouter.
     */
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
            // Mettre à jour la position des bubbles.
            i = bubbles.listIterator();
            while (i.hasNext()) {
                bubble = i.next();
                Vector2d position = bubble.getPosition();
                Vector2d speed = bubble.getSpeed();
                bubble.setPosition(position.add(speed.multiply(delta)));
            }

            // Vérifier si la bulle intersecte une autre bulle.
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
                        if (!oldSpeed.equals(bubble.getSpeed())) {
                            for (PhysicsListener l : listeners)
                                l.bubbleToBubbleCollision(bubble, otherBubble);
                        }
                    }
                }
            }

            // Vérifier si la bulle entre en intersection avec un mur.
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

}