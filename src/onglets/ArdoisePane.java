package onglets;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import ardoise.DrawPanel;
import ardoise.Forme;
import ardoise.JdeDessinToolbar;

public class ArdoisePane extends JPanel {

    private final JdeDessinToolbar jtb; // La barre d'outils
    private final DrawPanel draw; // L'espace pour dessiner

    /*
     * Constructeur
     */
    public ArdoisePane() {
        /* Initialisation des variables d'instance */
        this.jtb = new JdeDessinToolbar();
        this.draw = new DrawPanel();

        /* Un layout manager de type BorderLayout */
        this.setLayout(new BorderLayout());

        /* Initialisation des écouteurs de la barre d'outils */
        this.listenToolbar(jtb);
        this.add(jtb, BorderLayout.NORTH);

        /* Ajout de l'espace de dessin */
        this.add(draw, BorderLayout.CENTER);
    }

    /**
     * Initialisation des écouteurs sur la barre d'outils
     *
     * @param jtb
     */
    private void listenToolbar(JdeDessinToolbar jtb) {
        /* Ajout des écouteurs sur les boutons */
        jtb.getTbEfface().addActionListener(e -> this.draw.erase());
        jtb.getTbCarre().addActionListener(e -> this.draw.setForme(Forme.CARRE));
        jtb.getTbRond().addActionListener(e -> this.draw.setForme(Forme.ROND));
        jtb.getTbChooseColor().addActionListener((e) -> {
            Color newColor = JColorChooser.showDialog(
                    ArdoisePane.this,
                    "Choisir une couleur",
                    Color.RED);
            if (newColor != null) {
                this.draw.setCouleur(newColor);
                this.jtb.getTbChooseColor().setBackground(newColor);
            }
        });
        jtb.getJcbChooseSize().addActionListener((e) -> {
            draw.setSize((Integer) jtb.getJcbChooseSize().getSelectedItem());
            draw.repaint();
        });
    }
}
