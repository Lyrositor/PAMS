import objects.Bubble;

interface PhysicsListener {
    void bubbleToBubbleCollision(Bubble bubble, Bubble otherBubble);

    void bubbleToWallCollision(Bubble bubble);
}
