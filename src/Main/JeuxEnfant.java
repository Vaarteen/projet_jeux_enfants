package Main;

import onglets.AdminPane;
import onglets.ArdoisePane;
import onglets.CalculPane;
import onglets.QuestionPane;
import utils.Utils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;

public class JeuxEnfant extends JFrame {

    // Les constantes
    private static final String CONF_FILE = "jeuxEnfantConf.txt"; // Le fichier de configuration

    // Les variables d'instance
    private final JTabbedPane content; // Le contenu global : des onglets
    private final ArdoisePane dessin; // Le contenu de l'onglet pour dessiner
    private final CalculPane calcul; // Le contenu de l'onglet pour le calcul
    private final QuestionPane question; // Le contenu de l'onglet pour les questions
    private final AdminPane admin; // Le contenu de l'onglet d'administration
    private final JdeMenuBar jmb; // La barre de menu
    private int niveau; // Le niveau du jeu
    private String adminPassword; // Le mot de passe d'administration (modifiable donc pas final)

    /*
     * Constructeur
     */
    public JeuxEnfant() {
        /* Initialisation de la fenêtre et des variables d'instance */
        super("Jeux d'enfants"); // Super-constructeur
        this.niveau = 1; // Niveau le plus faible par défaut
        this.content = new JTabbedPane();
        this.dessin = new ArdoisePane();
        this.calcul = new CalculPane(this.niveau);
        this.question = new QuestionPane(this.niveau);
        this.admin = new AdminPane(this.niveau);
        this.jmb = new JdeMenuBar();

        /* Initialisation du mot de passe à partir d'un fichier de properties 
           Par défaut c'est "admin"... puissant, non ? */
        this.adminPassword = this.findAdminPassword();

        /* L'onglet d'administration disparaît dès qu'on le quitte */
        this.content.addChangeListener((e) -> {
            if (content.getSelectedComponent() != admin) {
                content.remove(admin);
            }
        });

        /* Configuration de la fenêtre */
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // La fermeture
        this.setContentPane(content); // Ajout du contenu
        this.listenMenuBar(jmb); // Ajout des écouteurs sur la barre de menu
        this.setJMenuBar(jmb); // Ajout de la barre de menu

        /* Ajout des onglets */
        this.content.addTab("Dessin", dessin);
        this.content.addTab("Calcul", calcul);
        this.content.addTab("Question", question);

        /* Affichage au centre de l'écran */
        // Récupération de la taille de l'écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(500, 600); // Taille de la fenêtre
        this.setResizable(false); // Fenêtre non-redimensionnable
        // On positionne la fenêtre au milieu de l'écran
        this.setLocation(
                (screenSize.width - this.getWidth()) / 2,
                (screenSize.height - this.getHeight()) / 2
        );
        this.setVisible(true); // Affichage de la fenêtre
    }

    /**
     * Méthode d'initialisation des écouteurs sur la barre de menu
     *
     * @param jmb
     */
    private void listenMenuBar(JdeMenuBar jmb) {
        /* Les écouteurs et leurs actions */
        jmb.dessinMenuItem.addActionListener(e -> content.setSelectedComponent(dessin));
        jmb.calculMenuItem.addActionListener(e -> content.setSelectedComponent(calcul));
        jmb.questionMenuItem.addActionListener(e -> content.setSelectedComponent(question));
        jmb.niv1MenuItem.addActionListener(e -> setNiveau(1));
        jmb.niv2MenuItem.addActionListener(e -> setNiveau(2));
        jmb.ajoutQuestionMenuItem.addActionListener(e -> changeQuestions());
        jmb.modifAdminPasswordMenuItem.addActionListener(e -> changePassword());

        /* On n'ajoute le menu d'administration que si le mot de passe
           administrateur existe, donc si on a trouvé le fichier de
           configuration */
        if (this.adminPassword != null) {
            jmb.addAdminMenu();
        }
    }

    /**
     * Demande de modification des questions. Nécessite d'avoir le mot de passe
     * administrateur pour afficher l'onglet correspondant
     */
    private void changeQuestions() {
        // Sur annulation on ne fait rien
        Boolean isAdmin = verifyAccessRights();
        if (isAdmin == null) {
            return;
        }
        // Si on a les droits => ajout et sélectionde l'onglet d'administration
        if (isAdmin) {
            this.content.addTab("Administration", admin);
            this.content.setSelectedComponent(admin);
        } else { // Sinon on sort avec un message d'erreur
            Utils.showAlertMessage("Mot de passe erroné.");
        }
    }

    /**
     * Changement de mot de passe de l'administrateur
     */
    private void changePassword() {
        // Sur annulation on ne fait rien
        Boolean isAdmin = verifyAccessRights();
        if (isAdmin == null) {
            return;
        }
        // Si on n'a pas les droits, on sort avec un message d'erreur
        if (!isAdmin) {
            Utils.showAlertMessage("Mot de passe erroné.");
            return;
        }
        /* Sinon on demande le nouveau mot de passe.
           On construit une jolie fenêtre de dialogue puisqu'on ne
           peut pas avoir des champs de type "Password" simplement dans
           une fenêtre de dialogue JOptionPane */
        // Le panneau
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        // Une première ligne pour entrer le nouveau mot de passe
        JPanel firstPwdPanel = new JPanel(new BorderLayout());
        JLabel firstPwdLabel = new JLabel("Veuillez donner le nouveau mot de passe d'administration ");
        JPasswordField firstPwdField = new JPasswordField(20);
        firstPwdPanel.add(firstPwdLabel, BorderLayout.WEST);
        firstPwdPanel.add(firstPwdField, BorderLayout.EAST);
        // Une seconde ligne pour confirmer le nouveau mot de passe
        JPanel secondPwdPanel = new JPanel(new BorderLayout());
        JLabel secondPwdLabel = new JLabel("Veuillez confirmer le nouveau mot de passe d'administration ");
        JPasswordField secondPwdField = new JPasswordField(20);
        secondPwdPanel.add(secondPwdLabel, BorderLayout.WEST);
        secondPwdPanel.add(secondPwdField, BorderLayout.EAST);
        panel.add(firstPwdPanel);
        panel.add(secondPwdPanel);
        // Les textes des boutons
        String[] options = new String[]{"OK", "Cancel"};
        // On affiche la fenêtre de dialogue qui retourne un int
        // correspondant à l'option choisie (le bouton cliqué)
        int option = JOptionPane.showOptionDialog(null, // Parent
                panel, // Contenu
                "Vérification d'accès", // Titre
                JOptionPane.NO_OPTION, // Boutons "normaux"
                JOptionPane.QUESTION_MESSAGE, // Type de message
                null, // Icône
                options, // Boutons à construire
                options[0]); // Bouton sélectionné par défaut
        // Si on clique sur OK
        if (option == 0) {
            // On récupère les deux mots de passe proposés
            char[] password1 = firstPwdField.getPassword();
            char[] password2 = secondPwdField.getPassword();
            String proposedPassword1 = new String(password1);
            String proposedPassword2 = new String(password2);
            // S'ils concordent
            if (proposedPassword1.equals(proposedPassword2)) {
                // On l'encode
                String encodedPassword = get_SHA_512_SecurePassword(proposedPassword1);
                // Et on le stocke
                this.savePassword(encodedPassword);
            }
        }
    }

    /**
     * Enregistrement du mot de passe administrateur chiffré dans le fichier de
     * configuration
     *
     * @param encodedPassword
     */
    private void savePassword(String encodedPassword) {
        Properties adminProp = new Properties();
        try (FileReader fr = new FileReader(CONF_FILE)) {
            // On récupère le fichier de configuration dans un objet Properties
            adminProp.load(fr);
            // On stocke le nouveau mot de passe dans l'objet Properties
            adminProp.setProperty("AdminPassword", encodedPassword);
            // On ouvre le fichier de configuration en écriture
            FileWriter fw = new FileWriter(CONF_FILE);
            // On y écrit la configuration
            adminProp.store(fw, "Modification du mot de passe d'administration");
            // On n'oublie pas de modifier le mot de passe chiffré
            // dans l'objet en cours...
            this.adminPassword = encodedPassword;
            // Et on informe l'utilisateur
            Utils.showPlainMessage("Mot de passe administrateur modifié");
        } catch (IOException ex) {
            Utils.showAlertMessage("Malheureusment le programme n'a pas trouvé le fichier"
                    + " de configuration de l'application.");
        }
    }

    /**
     * Vérification du mot de passe. Retourne null sur annulation, un booléen
     * sinon
     *
     * @return
     */
    private Boolean verifyAccessRights() {
        /* On récupère le mot de passe proposé, on le crypte en SHA-512 et on le
          compare avec le mot de passe récupéré depuis le fichier de
          configuration des properties (déjà encodé, bien sûr !) */
        Boolean ret = null;
        /* On construit une fenêtre de dialogue avec un champ de type password */
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Veuillez donner le mot de passe d'administration");
        JPasswordField pass = new JPasswordField(20);
        panel.add(label);
        panel.add(pass);
        // Les boutons
        String[] options = new String[]{"OK", "Cancel"};
        // Affichage de la fenêtre
        int option = JOptionPane.showOptionDialog(null,
                panel,
                "Vérification d'accès",
                JOptionPane.NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        // Si onclique sur OK
        if (option == 0) {
            // On récupère le mot de passe entré
            char[] password = pass.getPassword();
            String proposedPassword = new String(password);
            // On l'encode
            String encodedPassword = get_SHA_512_SecurePassword(proposedPassword);
            // On le compare avec le mot de passe stocké dans l'objet en cours
            ret = (encodedPassword.equals(this.adminPassword));
        }
        // On retourne la comparaison, ou null sur annulation
        return ret;
    }

    /**
     * Chiffrement en SHA-512 d'une String fournie en paramètre
     *
     * @param passwordToHash
     * @return
     */
    private static String get_SHA_512_SecurePassword(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(JeuxEnfant.class.getName()).log(Level.SEVERE, null, ex);
        }
        return generatedPassword;
    }

    /**
     * Récupération du fichier de properties, qui contient le mot de passe
     * administrateur chiffré.
     *
     * @return
     */
    private String findAdminPassword() {
        String retValue = null;
        try (FileReader fr = new FileReader(CONF_FILE)) {
            Properties adminProp = new Properties();
            adminProp.load(fr);
            retValue = adminProp.getProperty("AdminPassword");
        } catch (IOException ex) {
            String msg = "Malheureusment le programme n'a pas trouvé le fichier"
                    + " de configuration de l'application.\n"
                    + "Il sera donc en particulier impossible d'administrer le "
                    + "programme et de modifier ou d'ajouter des questions.";
            Utils.showAlertMessage(msg);
        }
        return retValue;
    }

    /* Accesseurs */
    /**
     * Mutateur de la variable niveau
     *
     * @param niveau
     */
    private void setNiveau(int niveau) {
        this.niveau = niveau;
        this.calcul.changeLevel(niveau);
        this.question.changeLevel(niveau);
    }

    /**
     * Accesseur de la variable content, qui contient le contenu de la fenêtre
     * de jeu
     *
     * @return Le panneau à onglet des différents jeux
     */
    public JTabbedPane getContent() {
        return this.content;
    }
}
