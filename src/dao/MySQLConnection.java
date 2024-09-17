package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MySQLConnection {

    /*
     * Configuration de la connexion
     */
    private static Properties config;

    /*
     * Objet Connection - Singleton
     */
    private static Connection connection;

    /*
     * Constructeur privé pour éviter l'instanciation
     */
    private MySQLConnection() {
    }


    /**
     * Méthode qui va nous retourner notre instance et la créer si elle n'existe
     * pas (singleton)
     *
     * @return la connexion vers la base de donnée ou null
     */
    public static Connection getInstance() {
        if (connection == null) {
            loadConfig();
            try {
                String url = "jdbc:" + config.getProperty("sgbd")
                        + "://" + config.getProperty("host")
                        + ":" + config.getProperty("port")
                        + "/" + config.getProperty("database");
                connection = DriverManager.getConnection(url, config);
                createDB();
            } catch (SQLException ex) {
                Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return connection;
    }

    public static void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
            }
        }
    }

    private static void loadConfig() {
        config = new Properties();
        try (InputStream in = MySQLConnection.class.getResourceAsStream("/ressources/config.properties")) {
            config.load(in);
        } catch (IOException ex) {
            Logger.getLogger(MySQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void createDB() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
