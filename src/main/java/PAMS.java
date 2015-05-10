import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class PAMS extends JFrame {

    private static final int[] DIMENSIONS = {1000, 400};
    private static final String TITLE = "PAMS";

    // Variables temporaires.
    int vitesse = 3;
    int nombreBalle = 5;

    // Éléments graphiques.
    private BorderLayout principal;
    private JButton vitessePlus = new JButton(" Vitesse +");
    private JButton vitesseMoins = new JButton(" Vitesse -");
    private JButton ajoutBalle = new JButton("   Balle +   ");
    private JButton retraitBalle = new JButton("   Balle -   ");
    private JLabel test = new JLabel("LABEL TEST");
    private JLabel test2 = new JLabel("LABEL TEST2");
    private JLabel test3 = new JLabel("LABEL TEST3");

    private BubblesPanel canvas;
    private PhysicsEngine physics;
    private SoundEngine sound;

    public PAMS() {
        super();

        // Démarrer les sous-systèmes.
        physics = new PhysicsEngine();
        canvas = setupCanvas();
        sound = new SoundEngine();
        sound.start();

        // Affiche la fenêtre et lancer la simulation.
        setVisible(true);
        run();
    }

    public static void main(String[] args) {
        PAMS application = new PAMS();
    }

    /**
     * Initialise tous les composants de la fenêtre et l'affiche.
     */
    private BubblesPanel setupCanvas() {
        principal = new BorderLayout();
        Container content = getContentPane();
        content.setLayout(principal);
        setSize(new Dimension(DIMENSIONS[0], DIMENSIONS[1]));
        setTitle(TITLE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Division de la fenetre en trois colonnes

        // Création du bouton dans la colonne de gauche
        // On définit le layout en lui indiquant qu'il travaillera en ligne.
        JPanel ligneVitesse = new JPanel();
        ligneVitesse.setLayout(new BoxLayout(ligneVitesse, BoxLayout.LINE_AXIS));
        ligneVitesse.add(vitessePlus);
        ligneVitesse.add(vitesseMoins);

        // Idem pour cette ligne.
        JPanel ligneAjout = new JPanel();
        ligneAjout.setLayout(new BoxLayout(ligneAjout, BoxLayout.LINE_AXIS));
        ligneAjout.add(ajoutBalle);
        ligneAjout.add(retraitBalle);

        // On positionne maintenant le tout en colonne.
        JPanel colonne = new JPanel();
        colonne.setLayout(new BoxLayout(colonne, BoxLayout.PAGE_AXIS));
        colonne.add(ligneVitesse);
        colonne.add(ligneAjout);

        vitessePlus.addActionListener(new VitessePlusListener());
        vitesseMoins.addActionListener(new VitesseMoinsListener());
        ajoutBalle.addActionListener(new AjoutBalleListener());
        retraitBalle.addActionListener(new RetraitBalleListener());

        content.add(colonne, BorderLayout.EAST);
        content.add(test, BorderLayout.SOUTH);
        content.add(test3, BorderLayout.WEST);

        // Création d'une barre de menu
        JMenuBar barreMenu = new JMenuBar();
        JMenu fichierMenu = new JMenu("Fichier");
        JMenu editionMenu = new JMenu("Edition");
        JMenuItem nouveau = new JMenuItem("Nouveau");
        JMenuItem quitter = new JMenuItem("Quitter");
        JMenuItem sauvegarder = new JMenuItem("Sauvegarder");

        // Ajout du menu.
        content.add(barreMenu, BorderLayout.NORTH);
        barreMenu.add(fichierMenu);
        barreMenu.add(editionMenu);
        fichierMenu.add(nouveau);
        fichierMenu.add(quitter);
        editionMenu.add(sauvegarder);

        BubblesPanel panel = new BubblesPanel(physics, 300, 200);
        content.add(panel, BorderLayout.CENTER);

        return panel;
    }

    public void run() {
        double delta;
        double previous = new Date().getTime();

        while (true) {
            // Mettre à jour le temps écoulé.
            delta = new Date().getTime() - previous;
            previous = new Date().getTime();

            // Mettre à jour la simulation physique.
            physics.update(delta);

            // Mettre à jour le canvas.
            canvas.repaint();
        }
    }

    public class VitessePlusListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            vitesse = vitesse + 1;
            test.setText("la vitesse +1");
        }
    }

    public class VitesseMoinsListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (vitesse >= 1) {
                vitesse--;
                test.setText("la vitesse -1");
            } else
                test.setText("la vitesse =1");
        }
    }

    public class AjoutBalleListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            nombreBalle = nombreBalle + 1;
            test.setText("nombreBalle + 1");
        }
    }

    public class RetraitBalleListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (nombreBalle >= 1) {
                nombreBalle = nombreBalle - 1;
                test.setText("nombreBalle - 1");
            } else
                test.setText("nombreBalle = 1");
        }
    }

}