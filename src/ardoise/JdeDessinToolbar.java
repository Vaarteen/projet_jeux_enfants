package ardoise;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JToolBar;

public class JdeDessinToolbar extends JToolBar {

    /*
     * Les variables d'instance
     */
    private final JButton tbCarre; // Image sélection pointe carrée
    private final JButton tbRond; // Image sélection pointe ronde
    private final JButton tbEfface; // Bouton d'effacement
    private final JButton tbChooseColor; // Un bouton pour ouvrir le JColorChooser
    private final JColorChooser jcc; // Panneau de choix de la couleur
    private final JComboBox jcbChooseSize; // Liste déroulante pour choisir la taille du pointeur

    /*
     * Constructeur
     */
    public JdeDessinToolbar() {
        /* Les boutons de la barre d'outils avec leurs icônes */
        this.tbCarre = new JButton(new ImageIcon("images/carre.jpg"));
        this.tbRond = new JButton(new ImageIcon("images/rond.jpg"));
        this.tbEfface = new JButton(new ImageIcon("images/efface.jpg"));
        this.tbChooseColor = new JButton("     ");

        /* Le sélecteur de couleur */
        this.jcc = new JColorChooser(Color.GREEN);

        /* Le sélecteur de taille */
        ArrayList<Integer> proposedSizes = new ArrayList<>();
        for (int i = 2; i <= 24; i = i + 2) {
            proposedSizes.add(i);
        }
        this.jcbChooseSize = new JComboBox(proposedSizes.toArray());
        this.jcbChooseSize.setSelectedIndex(proposedSizes.indexOf(10));

        /* Ajout du bouton d'effacement */
        this.tbEfface.setToolTipText("Effacer");
        this.add(tbEfface);
        this.addSeparator();

        /* Ajout des formes */
        this.tbCarre.setToolTipText("Pointe carrée");
        this.add(tbCarre);
        this.tbRond.setToolTipText("Pointe ronde");
        this.add(tbRond);
        this.addSeparator();

        /* Ajout du sélecteur de couleurs */
        this.addSeparator();
        this.tbChooseColor.setToolTipText("Choisir la couleur");
        this.add(tbChooseColor);
        this.tbChooseColor.setBackground(Color.RED);

        /* Ajout du sélecteur de taille */
        this.addSeparator();
        this.add(this.jcbChooseSize);
    }

    /*
     * Accesseurs
     */
    /**
     * Accesseur de la variable tbCarre
     *
     * @return Le bouton image de sélection de la pointe carrée
     */
    public JButton getTbCarre() {
        return tbCarre;
    }

    /**
     * Accesseur de la variable tbRond
     *
     * @return Le bouton image de sélection de la pointe ronde
     */
    public JButton getTbRond() {
        return tbRond;
    }

    /**
     * Accesseur de la variable tbEfface
     *
     * @return Le bouton image d'effacement du dessin
     */
    public JButton getTbEfface() {
        return tbEfface;
    }

    /**
     * Accesseur de la variable tbChooseColor
     *
     * @return Le bouton image de sélection de la couleur
     */
    public JButton getTbChooseColor() {
        return tbChooseColor;
    }

    /**
     * Accesseur de la variable jcbChooseSize
     *
     * @return La liste déroulante de sélection de la taille du pointeur
     */
    public JComboBox getJcbChooseSize() {
        return jcbChooseSize;
    }
}
