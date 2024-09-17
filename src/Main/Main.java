package Main;

import dao.MySQLConnection;

/**
 * Jeux pour les enfants
 *
 * @author Herbert
 */
public class Main {

    public static void main(String[] args) {
        // Créer la DB en mémoire
        MySQLConnection.getInstance();
        JeuxEnfant j = new JeuxEnfant();
        // Fermer proprement la connexion
        MySQLConnection.close();
    }

}
