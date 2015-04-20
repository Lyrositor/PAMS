import javax.swing.*;
import java.awt.*;

public class PAMS extends JFrame {

    private static final int[] DIMENSIONS = {600, 400};
    private static final String TITLE = "PAMS";
    private BorderLayout principal;
    private Simulation simulation;

    public PAMS() {
        super();

        // Démarrer les sous-systèmes.
        SoundEngine sound = new SoundEngine();
        sound.start();

        // Préparer la fenêtre pour l'affichage, puis l'afficher.
        setupWindow();

        // Lancer la simulation.
        simulation = new Simulation(sound);
        simulation.run();
    }

    public static void main(String[] args) {
        PAMS application = new PAMS();
    }

    /**
     * Initialise tous les composants de la fenêtre et l'affiche.
     */
    private void setupWindow() {
        principal = new BorderLayout();
        Container content = this.getContentPane();
        content.setLayout(principal);
        this.setSize(new Dimension(DIMENSIONS[0], DIMENSIONS[1]));
        this.setTitle(TITLE);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Division de la fenetre en trois colonnes

        // Création du bouton dans la colonne de gauche
        // On définit le layout en lui indiquant qu'il travaillera en ligne.
        JPanel ligneVitesse = new JPanel();
        ligneVitesse.setLayout(new BoxLayout(ligneVitesse, BoxLayout.LINE_AXIS));
        ligneVitesse.add(new JButton("Vitesse +"));
        ligneVitesse.add(new JButton("Vitesse -"));

        // Idem pour cette ligne.
        JPanel ligneAjout = new JPanel();
        ligneAjout.setLayout(new BoxLayout(ligneAjout, BoxLayout.LINE_AXIS));
        ligneAjout.add(new JButton("Balle +"));
        ligneAjout.add(new JButton("Balle -"));

        // On positionne maintenant le tout en colonne.
        JPanel colonne = new JPanel();
        colonne.setLayout(new BoxLayout(colonne, BoxLayout.PAGE_AXIS));
        colonne.add(ligneVitesse);
        colonne.add(ligneAjout);

        this.getContentPane().add(colonne, BorderLayout.EAST);
        this.setVisible(true);
    }
}