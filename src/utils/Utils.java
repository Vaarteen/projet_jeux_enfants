package utils;

import javax.swing.JOptionPane;

public class Utils {

    /**
     * Affiche une fenêtre d'information par défaut avec le message passé en
     * paramètre
     *
     * @param msg Le message à afficher
     */
    public static void showPlainMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Information", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Affiche une fenêtre d'alerte par défaut avec le message passé en
     * paramètre
     *
     * @param msg Le message à afficher
     */
    public static void showAlertMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Alerte", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Supprime les accents d'une chaîne de caractère passée en paramètre et
     * retourne la nouvelle chaîne
     *
     * @param s La chaîne de caractères à traiter
     * @return La chaîne de caractères sans ses accents
     */
    public static String suppressAccents(String s) {
        return s.replaceAll("[aàâä]", "a")
                .replaceAll("[éèëê]", "e")
                .replaceAll("[iïîì]", "i")
                .replaceAll("[oôöò]", "o")
                .replaceAll("[uûüù]", "u");
    }
}
