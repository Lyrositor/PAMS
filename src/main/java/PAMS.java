import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PAMS extends JFrame {

    private static final int[] DIMENSIONS = {1000, 400};
    private static final String TITLE = "PAMS";
    // Variables temporaires.
    int vitesse = 3;
    int nombreBalle = 5;
    private BorderLayout principal;
    private Simulation simulation;
    private JButton vitessePlus = new JButton(" Vitesse +");
    private JButton vitesseMoins = new JButton(" Vitesse -");
    private JButton ajoutBalle = new JButton("   Balle +   ");
    private JButton retraitBalle = new JButton("   Balle -   ");
    private JLabel test = new JLabel("LABEL TEST");
    private JLabel test2 = new JLabel("LABEL TEST2");
    private JLabel test3 = new JLabel("LABEL TEST3");
    private JPanel dessin;

    public PAMS() {
        super();

        // Préparer la fenêtre pour l'affichage, puis l'afficher.
        JPanel panel = setupWindow();

        // Lancer la simulation.
        simulation = new Simulation(panel);
        simulation.run();
    }

    public static void main(String[] args) {
        PAMS application = new PAMS();
    }

    /**
     * Initialise tous les composants de la fenêtre et l'affiche.
     */
    private JPanel setupWindow() {
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

        //donne a l'ecriture la couleur bleue
        //vitessePlus.setForeground(Color.blue);
        //vitessePlus.setBackground(Color.blue);


        this.getContentPane().add(colonne, BorderLayout.EAST);

        this.getContentPane().add(test, BorderLayout.SOUTH);
        // this.getContentPane().add(test2, BorderLayout.SOUTH);
        this.getContentPane().add(test3, BorderLayout.WEST);

        dessin = new JPanel();
        dessin.setSize(300, 200);
        this.getContentPane().add(dessin, BorderLayout.CENTER);

        // creation d'une barre de menu
        JMenuBar barreMenu = new JMenuBar();

        this.getContentPane().add(barreMenu, BorderLayout.NORTH);

        JMenu fichierMenu = new JMenu("Fichier");
        JMenu editionMenu = new JMenu("Edition");
        barreMenu.add(fichierMenu);
        barreMenu.add(editionMenu);

        // creation des items du menu
        JMenuItem nouveau = new JMenuItem("Nouveau");
        JMenuItem quitter = new JMenuItem("Quitter");
        JMenuItem sauvegarder = new JMenuItem("Sauvegarder");

        fichierMenu.add(nouveau);
        fichierMenu.add(quitter);
        editionMenu.add(sauvegarder);

        this.setVisible(true);


        return dessin;
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics g1 = dessin.getGraphics();
        g1.setColor(Color.blue);
        g1.fillRect(0, 0, 1000, 1000);

        //g.drawRect(0,0,getSize().width - 1,getSize().height - 1);
        //g.drawString(message,50,150);
    }

    public class VitessePlusListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            vitesse = vitesse + 1;
            test.setText("la vitesse +1");
        }

    }

    public class VitesseMoinsListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (vitesse >= 1) { // je veux que les balles soient toujours en mouvement
                vitesse = vitesse - 1;
                test.setText("la vitesse -1");
            } else {
                test.setText("la vitesse =1");
            }
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
            if (nombreBalle >= 1) { // je veux qu'il y ait toujours une balle
                nombreBalle = nombreBalle - 1;
                test.setText("nombreBalle - 1");
            } else {
                test.setText("nombreBalle =1");
            }
        }

    }
}