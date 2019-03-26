package onglets;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import utils.Utils;

public class CalculPane extends JPanel implements ChangeableLevel {
    /*
     * Les variables d'instance
     */
    private final JLabel labelNiveau, // Le label indiquant le niveau sélectionné
            labelCalcul; // Le label montrant le calcul à effectuer
    private JTextField jtfProposition; // Le champ de réponse
    private final JButton jbVerifier, // Le bouton de vérification de la proposition
            jbSolution, // Le bouton montrant la solution
            jbAutre; // Le bouton pour demander un autre calcul
    private String question; // La question (le calcul) en cours
    private Integer reponse; // La réponse à la question
    private int niveau; // Le niveau en cours

    /*
     * Constructeur
     */
    public CalculPane(int niveau) {
        /* Initialisation des variables d'instance */
        // Les labels
        this.labelNiveau = new JLabel();
        this.labelCalcul = new JLabel();
        // Le champ de réponse avec sa popup d'information
        this.jtfProposition = new JTextField(5);
        this.jtfProposition.setToolTipText("A ton avis, ça fait combien ?\nEntres la valeur ici");
        this.niveau = niveau;
        // Les boutons
        this.jbVerifier = new JButton("Vérifier");
        this.jbSolution = new JButton("Solution");
        this.jbAutre = new JButton("Autre calcul");
        // Positionnement du niveau
        this.changeLevel(niveau);

        /* Les écouteurs */
        // On écoute les boutons
        this.jbVerifier.addActionListener((e) -> verifyCalculation());
        this.jbSolution.addActionListener((e) -> jtfProposition.setText(reponse.toString()));
        this.jbAutre.addActionListener((e) -> generateCalculation());
        // On écoute le clavier également sur le champ de réponse.
        // appuyer sur Enter provoque la vérification du calcul, appuyer sur
        // autre chose qu'un chiffre ou un effacement provoque une popup
        // d'erreur
        jtfProposition.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Character enteredChar = e.getKeyChar();
                if (enteredChar == '\n') { // appui sur Enter
                    jbVerifier.doClick();
                } else { // Appui sur autre chose que effacement et chiffre
                    if (enteredChar != '\b' && !"0123456789".contains(enteredChar.toString())) {
                        informBadChar();
                    }
                }
            }
        });

        // On écoute aussi la fenêtre pour générer un calcul lors de son
        // affichage
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                // Positionnement de la question et de sa réponse
                generateCalculation();
            }
        });

        /* Ajout des éléments au JPanel */
        this.setLayout(new BorderLayout());
        // Chaque zone a son propre layout
        JPanel jpNorth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel jpCenter = new JPanel(new FlowLayout());
        JPanel jpSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // Ajout des composants dans les zones
        jpNorth.add(this.labelNiveau);
        jpCenter.add(this.labelCalcul);
        jpCenter.add(this.jtfProposition);
        jpCenter.add(this.jbVerifier);
        jpSouth.add(this.jbSolution);
        jpSouth.add(this.jbAutre);
        // Ajout des zones au JPanel
        this.add(jpNorth, BorderLayout.NORTH);
        this.add(jpCenter, BorderLayout.CENTER);
        this.add(jpSouth, BorderLayout.SOUTH);
    }

    /**
     * Génération d'un labelCalcul aléatoire dans le label correspondant
     */
    private void generateCalculation() {
        HashMap<String, Object> questionAnswer;
        if (this.niveau == 1) {
            questionAnswer = this.generateLevel1Calculation();
        } else {
            questionAnswer = this.generateLevel2Calculation();
        }
        /* Récupération de la question et de la réponse */
        this.question = (String) questionAnswer.get("question");
        this.reponse = (Integer) questionAnswer.get("answer");
        this.showQuestion();
    }

    /**
     * Affiche la question
     */
    private void showQuestion() {
        this.labelCalcul.setText(this.question);
        this.jtfProposition.setText("");
        this.jtfProposition.requestFocusInWindow();
    }

    /**
     * Génération d'un labelCalcul aléatoire de niveau 2. Retourne un tableau
     * (clé, valeur) contenant la question et la réponse.
     *
     * @return
     */
    private HashMap<String, Object> generateLevel1Calculation() {
        HashMap<String, Object> questionAnswer = new HashMap<>();
        String q; // La question
        Integer a; // La réponse
        Random rand = new Random(); // Initialisation de la génération aléatoire
        int op1 = rand.nextInt(11); // Un entier aléatoire en 0 et 10
        int op2 = rand.nextInt(11); // Un entier aléatoire entre 0 et 10
        boolean isSomme = rand.nextBoolean(); // On tire au hasard l'opération
        if (isSomme) { // Si c'est une somme
            q = "" + op1 + " + " + op2 + " = ";
            a = op1 + op2;
        } else { // sinon c'est une différence, il faut mettre les opérande dans l'ordre
            int max = Math.max(op1, op2);
            int min = Math.min(op1, op2);
            q = "" + max + " - "
                    + min + " = ";
            a = max - min;
        }
        questionAnswer.put("question", q);
        questionAnswer.put("answer", a);
        return questionAnswer;
    }

    /**
     * Génération d'un labelCalcul aléatoire de niveau 2. Retourne un tableau
     * (clé, valeur) contenant la question et la réponse.
     *
     * @return
     */
    private HashMap<String, Object> generateLevel2Calculation() {
        HashMap<String, Object> questionAnswer = new HashMap<>();
        String q = null; // La question
        Integer a = null; // La réponse
        Random rand = new Random();
        int op1, op2;
        int operation = rand.nextInt(3); // 0 = somme, 1 = différence, 2 = produit
        switch (operation) {
            case 0: // une somme
                op1 = rand.nextInt(1000); // nombre aléatoire à 3 chiffres
                op2 = rand.nextInt(1000); // nombre aléatoire à 3 chiffres
                q = "" + op1 + " + " + op2 + " = ";
                a = op1 + op2;
                break;
            case 1: // une différence
                op1 = rand.nextInt(1000); // nombre aléatoire à 3 chiffres
                op2 = rand.nextInt(1000); // nombre aléatoire à 3 chiffres
                q = "" + op1 + " - " + op2 + " = ";
                a = op1 - op2;
                break;
            case 2: // un produit
                op1 = rand.nextInt(10); // nombre aléatoire à 1 chiffre
                op2 = rand.nextInt(10); // nombre aléatoire à 1 chiffre
                q = "" + op1 + " x " + op2 + " = ";
                a = op1 * op2;
        }
        questionAnswer.put("question", q);
        questionAnswer.put("answer", a);
        return questionAnswer;
    }

    /**
     * Vérification du calcul. On sait qu'il y a un entier dedans puisqu'on ne
     * peut pas saisir autre chose que des chiffres dans le champ de réponse
     */
    private void verifyCalculation() {
        String temp = jtfProposition.getText();
        if (temp.isEmpty()) {
            Utils.showAlertMessage("Il faudrait une réponse");
        } else {
            Integer prop = new Integer(this.jtfProposition.getText());
            if (prop.equals(this.reponse)) {
                Utils.showPlainMessage("Bravo, tu as bien calculé !");
                // Et on génère une autre question
                this.generateCalculation();
            } else {
                Utils.showPlainMessage("Hélas ! Tu t'es trompé, essayes encore une fois.");
            }
        }
    }

    /**
     * Informe que la saisie est fausse et supprime le dernier caractère.     * Doit se faire dans un thread parallèle qui attend que l'édition du
     * JTextArea soit finie, sinon on a une exception. En fait ici je triche,
     * j'attends juste que tous les evènements soient finis avec la méthode
     * statique invokeLater();
     */
    private void informBadChar() {
        Runnable doCheck = new Runnable() {
            @Override
            public void run() {
                String prop = jtfProposition.getText();
                int propLength = prop.length();
                Utils.showPlainMessage("Tu ne dois utiliser que des chiffres de 0 à 9");
                if (propLength > 0) {
                    jtfProposition.setText(prop.substring(0, propLength - 1));
                }
            }
        };
        SwingUtilities.invokeLater(doCheck);
    }

    @Override
    public void changeLevel(int niveau) {
        this.labelNiveau.setText("Niveau " + niveau);
        this.niveau = niveau;
        this.generateCalculation();
    }
}
