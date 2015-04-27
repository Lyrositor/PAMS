import math.Vector2d;
import objects.Bubble;
import objects.Wall;

import java.awt.*;
import java.util.ArrayList;

class PhysicsEngine {

    private Wall[] walls;
    private ArrayList<Bubble> bubbles;

    private static final int NB_INITIAL_BUBBLES = 2;

    public PhysicsEngine() {
        // Cr�er les murs.
        Color tint = new Color(0, 0, 0);
        walls = new Wall[]{
                new Wall(10, 100, new Vector2d(-50, 0), tint),
                new Wall(10, 100, new Vector2d(50, 0), tint),
                new Wall(100, 10, new Vector2d(0, 50), tint),
                new Wall(100, 10, new Vector2d(0, -50), tint)
        };

        // Cr�er les bulles initiales.
        bubbles = new ArrayList<Bubble>();
        for (int i = 0; i < NB_INITIAL_BUBBLES; i++)
            bubbles.add(new Bubble(5.0, Vector2d.NULL, new Vector2d(5.0, 10.0)));
    }

    public void update(double delta) {
        // It�rer sur la liste des bulles.
        // Pour chacune, v�rifier si elle est en collision avec:
        // - soit une autre bulle,
        // - soit un mur.
        // Si c'est le cas, appliquer la modification de vitesse � la bulle et
        // continuer.
    }

}