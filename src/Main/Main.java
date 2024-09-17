package Main;

import dao.MySQLConnection;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Jeux pour les enfants
 *
 * @author Herbert
 */
public class Main {

    public static void main(String[] args) {
        JeuxEnfant j = new JeuxEnfant();
        // On ferme proprement la connexion
        j.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MySQLConnection.close();
            }

        });
    }

}
