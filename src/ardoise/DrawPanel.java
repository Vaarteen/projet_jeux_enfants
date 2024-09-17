package ardoise;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import javax.swing.JPanel;
import utils.Pair;

public class DrawPanel extends JPanel {

    // La couleur du pointeur
    private Color couleur = Color.RED;
    // La taille du pointeur
    private int size = 10;

    public void setSize(int size) {
        this.size = size;
    }
    // Ancienne position X/Y du pointeur, au départ hors de la fenêtre
    private int oldx = 10, oldy = -10;
    // La forme du pointeur
    private Forme forme = Forme.ROND;
    // On efface ?
    private boolean effacer = false;
    // Tous les points du tracé
    private LinkedList<Pair<Point, Boolean>> points = new LinkedList<>();

    // Le constructeur
    public DrawPanel() {
        // En cas de clic on ajoute le point sous le pointeur à la collection (boolean false)
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                points.add(new Line(new Point(e.getX() - size / 2, e.getY() - size / 2,
                        size, couleur, forme), false));
                repaint();
            }
        });
        // En cas de drag on ajoute un trait (boolean true)
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                points.add(new Line(new Point(e.getX() - size / 2, e.getY() - size / 2,
                        size, couleur, forme), true));
                repaint();
            }
        });
    }

    // Tracé du dessin
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        // Si on doit effacer, on ne dessine rien... CQFD
        // Et on change la valeur du booléen pour repartir de rien
        if (effacer) {
            effacer = false;
        } else {
            Graphics2D g2d = (Graphics2D) g;
            // On dessine tous les points de la collection
            points.forEach((e) -> {
                Point p = e.getKey();
                boolean b = e.getValue();
                g2d.setColor(p.getCouleur()); // On récupère la couleur
                g2d.setStroke(new BasicStroke(p.getSize(), // Taille du trait
                        BasicStroke.CAP_ROUND, // Forme du trait
                        BasicStroke.JOIN_ROUND // Forme des jointures
                ));
                if (b) {
                    // On trace un trait entre l'ancienne et la
                    // nouvelle position du point
                    g2d.drawLine(oldx + size / 2, oldy + size / 2,
                            p.getX() + size / 2, p.getY() + size / 2);
                    oldx = p.getX();
                    oldy = p.getY();
                } else { // On ne trace qu'un point
                    // On dessine le point en fonction de sa forme
                    if (p.getForme() == Forme.ROND) {
                        g2d.fillOval(p.getX(), p.getY(), p.getSize(), p.getSize());
                    } else {
                        g2d.fillRect(p.getX(), p.getY(), p.getSize(), p.getSize());
                    }
                    oldx = p.getX();
                    oldy = p.getY();
                }
            });
        }
    }

    // Effacement du contenu
    public void erase() {
        this.effacer = true;
        this.points.clear();
        repaint();
    }

    // Mutateurs
    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }

    public void setForme(Forme forme) {
        this.forme = forme;
    }

    private class Line extends Pair<Point, Boolean> {

        public Line(Point key, Boolean value) {
            super(key, value);
        }

    }
}
