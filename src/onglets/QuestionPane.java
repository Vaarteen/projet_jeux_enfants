package onglets;

import interfaces.ChangeableLevel;
import beans.Question;
import dao.DAOFactory;
import dao.QuestionDAO;
import utils.Utils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class QuestionPane extends JPanel implements ChangeableLevel {

    private static final long serialVersionUID = 1L;

    /*
     * Les variables d'instance
     */
    private final JLabel labelNiveau; // Le label indiquant le niveau sélectionné
    private final JTextArea labelQuestion; // Le texte montrant la question
    private JTextField jtfProposition; // Le champ de réponse
    private final JButton jbVerifier, // Le bouton de vérification de la proposition
            jbSolution, // Le bouton montrant la solution
            jbAutre; // Le bouton pour demander une autre question
    private String question; // La question en cours
    private String reponse; // La réponse à la question
    private int niveau; // Le niveau en cours
    public int nbQuestions; // Le nombre de questions en BD
    QuestionDAO questionDao; // Un DAO sur la table des questions
    ArrayList<Integer> questionIds; // La liste des id des questions de ce niveau

    /*
     * Constructeur
     */
    public QuestionPane(int niveau) {
        /* Initialisation des variables d'instance */
        // Les labels
        this.labelNiveau = new JLabel();
        this.labelQuestion = new JTextArea(1, 40);
        this.labelQuestion.setLineWrap(true);
        this.labelQuestion.setWrapStyleWord(true);
        this.labelQuestion.setOpaque(true);
        this.labelQuestion.setBackground(Color.ORANGE);
        // Le champ de réponse avec sa popup d'information
        this.jtfProposition = new JTextField(40);
        this.jtfProposition.setToolTipText("A ton avis, quelle est la réponse ?\nTapes-la ici");
        this.niveau = niveau;
        // Les boutons
        this.jbVerifier = new JButton("Vérifier");
        this.jbSolution = new JButton("Solution");
        this.jbAutre = new JButton("Autre question");
        // Les questions
        questionDao = DAOFactory.getQuestionDAO();
        // Positionnement du niveau et génération d'une question
        this.changeLevel(niveau);

        /* Les écouteurs */
        // On écoute les boutons
        this.jbVerifier.addActionListener((e) -> verifyResponse());
        this.jbSolution.addActionListener((e) -> jtfProposition.setText(reponse));
        this.jbAutre.addActionListener((e) -> generateQuestion());
        // On écoute le clavier également sur le champ de réponse.
        // Appuyer sur Enter provoque la vérification de la réponse
        jtfProposition.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Character enteredChar = e.getKeyChar();
                if (enteredChar == '\n') { // appui sur Enter
                    jbVerifier.doClick();
                }
            }
        });

        // On écoute aussi la fenêtre pour générer une question lors de son
        // affichage
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                // Positionnement de la question et de sa réponse
                generateQuestion();
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
        jpCenter.add(this.labelQuestion);
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
     * Vérifie la réponse donnée et informe l'utilisateur par une popup
     */
    private void verifyResponse() {
        String temp = jtfProposition.getText();
        if (temp.isEmpty()) {
            Utils.showPlainMessage("Il faudrait une réponse");
        } else {
            String prop = this.jtfProposition.getText();
            // On transforme tous les accents
            prop = Utils.suppressAccents(prop);
            // On fait la même chose sur la réponse...
            String rep = this.reponse;
            rep = Utils.suppressAccents(rep);
            if (prop.equalsIgnoreCase(rep)) {
                String msg = "Bravo, c'est la bonne réponse !";
                boolean isAccent = this.reponse.matches(".*[àâäéèëêïîìôöòûüù].*");
                boolean isExactResponse = this.jtfProposition.getText().equalsIgnoreCase(this.reponse);
                if (isAccent && isExactResponse) {
                    // Si la réponse contient des accents et qu'ils sont tous fournis
                    msg += "\nEt avec les accents en plus !\nFélicitation.";
                }
                Utils.showPlainMessage(msg);
                // Et on génère une autre question
                this.generateQuestion();
            } else {
                Utils.showPlainMessage("Hélas ! Tu t'es trompé, essayes encore une fois.");
            }
        }
    }

    /**
     * Pioche une question au hasard dans la BdD
     */
    private void generateQuestion() {
        Random rand = new Random();
        Integer askedQuestionNumber = rand.nextInt(this.nbQuestions);
        Question q = questionDao.find(this.questionIds.get(askedQuestionNumber));
        this.question = q.getQuestion();
        this.reponse = q.getReponse();
        this.showQuestion();
    }

    /**
     * Affiche la question
     */
    private void showQuestion() {
        this.labelQuestion.setText("Question : " + this.question);
        this.jtfProposition.setText("");
        this.jtfProposition.requestFocusInWindow();
    }

    /**
     * Retourne le nombre de questions stockées dans la BD, et stocke les id
     * correspondant dans la variable d'instance questionIds. On ne stocke pas
     * toutes les questions au cas où la BD     * serait TRES grosse, pour ne pas saturer la mémoire de l'ordinateur.
     *
     * @return
     */
    private int RequestQuestionIds() {
        this.questionIds = this.questionDao.findByLevel(this.niveau);
        return this.questionIds.size();
    }

    @Override
    public void changeLevel(int niveau) {
        this.labelNiveau.setText("Niveau " + niveau);
        this.niveau = niveau;
        this.nbQuestions = this.RequestQuestionIds();
        this.generateQuestion();
    }
}
