import math.Vector2d;
import objects.BoundingBox;
import objects.Bubble;
import objects.Wall;

import java.awt.*;
import java.util.ArrayList;

class PhysicsEngine {

    private static final int NB_INITIAL_BUBBLES = 2;
    private Wall[] walls;
    private ArrayList<Bubble> bubbles;

    public PhysicsEngine() {
        // Créer les murs.
        Color tint = new Color(0, 0, 0);
        walls = new Wall[]{
                new Wall(10, 100, new Vector2d(-50, 0), tint),
                new Wall(10, 100, new Vector2d(50, 0), tint),
                new Wall(100, 10, new Vector2d(0, 50), tint),
                new Wall(100, 10, new Vector2d(0, -50), tint)
        };

        // Créer les bulles initiales.
        bubbles = new ArrayList<Bubble>();
        for (int i = 0; i < NB_INITIAL_BUBBLES; i++)
            bubbles.add(new Bubble(5.0, Vector2d.NULL, new Vector2d(5.0, 10.0)));
    }

    public void update(double delta) {

        Bubble bubble;

        for (int i = 0; i < bubbles.size(); i++) {
            bubble = bubbles.get(i);
            Vector2d position = bubble.getPosition();
            Vector2d vitesse = bubble.getVitesse();
            bubble.setPosition(position.add(vitesse.multiply(delta)));
        }

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
        for (int i = 0; i < bubbles.size(); i++) {
            bubble = bubbles.get(i);
            for (int j = 0; j < walls.length; j++) {
                Wall wall = walls[j];
                Vector2d intersection = bubble.intersects(wall);
                if (intersection == null)
                    continue;
                Vector2d nouvelleVitesse = bubble.getVitesse();
                if (wall.getBounds().orientation() == BoundingBox.VERTICAL)
                    nouvelleVitesse.x = -nouvelleVitesse.x;
                else
                    nouvelleVitesse.y = -nouvelleVitesse.y;
                bubble.setVitesse(nouvelleVitesse);
            }
        }


        //1 actualiser la vitesse
        //2
        // Itérer sur la liste des bulles.
        // Pour chacune, vérifier si elle est en collision avec:
        // - soit une autre bulle,
        // - soit un mur.
        // Si c'est le cas, appliquer la modification de vitesse à la bulle et
        // continuer.
    }

}