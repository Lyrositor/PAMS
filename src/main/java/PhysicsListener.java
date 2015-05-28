import objects.Bubble;
import objects.Wall;

/**
 * To be implemented by classes who need to listen to events from PhysicsEngine.
 */
interface PhysicsListener {
    /**
     * Called when a bubble collides with another.
     *
     * @param bubble      The first bubble.
     * @param otherBubble The second bubble.
     */
    void bubbleToBubbleCollision(Bubble bubble, Bubble otherBubble);

    /**
     * Called when a bubble collides with a wall.
     *
     * @param bubble The colliding bubble.
     * @param wall The colliding wall.
     */
    void bubbleToWallCollision(Bubble bubble, Wall wall);
}