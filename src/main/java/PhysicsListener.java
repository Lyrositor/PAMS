import objects.Bubble;

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
     * Called when a bubble collides wiht a wall.
     *
     * @param bubble The colliding bubble.
     */
    void bubbleToWallCollision(Bubble bubble);
}