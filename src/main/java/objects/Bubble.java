package objects;

import math.Vector2d;

import java.awt.*;

public class Bubble extends PhysicsObject {

    private float rayon;

    public Bubble(float r, Vector2d p, Vector2d v, Vector2d a) {
        super(new BoundingBox(BoundsType.CIRCLE, r), p, v, a);
        rayon = r;
    }
    //il faudrait, comme on avait dit, lier certains paramètres entre eux : la couleur et la vitesse, la hauteur du son et la taille du rayon...

    public Vector2d getVitesse() {
        return vitesse;
    }

    public void setVitesse(Vector2d v) {
        vitesse = v;
    }

    public Vector2d getPosition() {
        return position;
    }

    public Color getColor() {
        return new Color(0, 0, 0);
    }

    public void setAcceleration(Vector2d a) {
        acceleration = a;
    }

    /**
     * Je suis partie du principe qu'on a un choc élastique entre deux bulles de masse identiques,
     * c'est à dire qu'après collision, chacune repart dans le sens opposé à sa direction initiale,
     * avec une vitesse de norme égale à l'initiale.
     * NOTE: Ce code doit être modifié, de façon à ce qu'il ne modifie que les
     * propriétés de this Bubble - tout autre changement sera à effectuer par
     * l'autre bulle.
     */
    /*public void collisionWith(PhysicsObject object) {
        float[] coord1 = b1.getPosition();
        float[] coord2 = b2.getPosition();

        if (coord1[0] == coord2[0] && coord1[1] == coord2[1]) {
            float tabVitesse1[] = b1.getVitesse();
            float tabVitesse2[] = b2.getVitesse();
            for (int i = 0; i < tabVitesse1.length; i++) {
                tabVitesse1[i] = -tabVitesse1[i];
                tabVitesse2[i] = -tabVitesse2[i];
            }
            b1.setVitesse(tabVitesse1);
            b2.setVitesse(tabVitesse2);
        }
    }*/
}