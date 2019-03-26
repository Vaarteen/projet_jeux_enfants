package ardoise;

import java.awt.Color;

public class Point {

    /**
     * La couleur du point
     */
    private Color couleur = Color.RED;
    /**
     * La taille du point
     */
    private int size = 10;
    /**
     * Position X, au départ hors de la fenêtre
     */
    private int x = -10;
    /**
     * Position y, au départ hors de la fenêtre
     */
    private int y = -10;
    /**
     * La forme du point
     */
    private Forme forme = Forme.ROND;

    /*
     * Constructeur
     */
    public Point(int x, int y, int taille, Color couleur, Forme forme) {
        this.x = x;
        this.y = y;
        this.size = taille;
        this.couleur = couleur;
        this.forme = forme;
    }

    /*
     * Accesseurs et mutateurs
     */
    /**
     * Accesseur de la variable couleur
     *
     * @return La couleur du pointeur
     */
    public Color getCouleur() {
        return couleur;
    }

    /**
     * Mutateur de la variable couleur
     *
     * @param couleur La couleur du pointeur
     */
    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }

    /**
     * Accesseur de la variable size
     *
     * @return La taille du pointeur
     */
    public int getSize() {
        return size;
    }

    /**
     * Mutateur de la variable size
     *
     * @param size La taille du pointeur
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Accesseur de la variable x
     *
     * @return L'abscisse du pointeur
     */
    public int getX() {
        return x;
    }

    /**
     * Mutateur de la variable x
     *
     * @param x L'abscisse du pointeur
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Accesseur de la variable y
     *
     * @return L'ordonnée du pointeur
     */
    public int getY() {
        return y;
    }

    /**
     * Mutateur de la variable y
     *
     * @param y L'ordonnée du pointeur
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Accesseur de la variable forme
     *
     * @return La forme du pointeur
     */
    public Forme getForme() {
        return forme;
    }

    /**
     * Mutateur de la variable forme
     *
     * @param forme La forme du pointeur
     */
    public void setForme(Forme forme) {
        this.forme = forme;
    }

}
