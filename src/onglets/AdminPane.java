package onglets;

import beans.Question;
import dao.DAOFactory;
import dao.QuestionDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AdminPane extends JPanel {

    JComboBox<Integer> levelChooser;
    JComboBox<String> questionChooser;
    JTextArea questionText;
    JTextField reponseText;
    JRadioButton level1, level2;
    ButtonGroup radioLevel;
    JButton jbOK,
            jbCancel;
    JLabel retour;
    Question question;
    QuestionDAO qdao;
    int level;

    /*
     * Constructeur
     */
    public AdminPane(int level) {
        /* Initialisation des variables */
        this.qdao = DAOFactory.getQuestionDAO();
        this.levelChooser = new JComboBox();
        this.questionChooser = new JComboBox();
        this.questionText = new JTextArea();
        this.reponseText = new JTextField(43);
        this.jbOK = new JButton("OK");
        this.jbCancel = new JButton("Annuler");
        this.retour = new JLabel(" ");
        this.level1 = new JRadioButton("niveau 1", true); // Sélectionné par défaut
        this.level2 = new JRadioButton("Niveau 2");
        this.level1.setActionCommand("1");
        this.level2.setActionCommand("2");
        // Les boutons radios sont dans un même groupe
        this.radioLevel = new ButtonGroup();
        this.radioLevel.add(this.level1);
        this.radioLevel.add(this.level2);
        // Affectation du niveau
        this.level = level;

        /* Initialisation des listes déroulantes */
        this.initLists(level);

        /* Création du panneau */
        this.initComponents();

        /* Ajout des listeners */
        this.initListeners();

        /* Mise à jour de la liste */
        this.changeLevel();
    }

    /**
     * Initialisation des listes déroulantes
     *
     * @param level
     */
    private void initLists(int level) {
        this.levelChooser = new JComboBox(new Integer[]{1, 2});
        this.updateQuestionList(level);
    }

    /**
     * Mise à jour de la liste des question selon le niveau demandé
     *
     * @param level
     */
    private void updateQuestionList(int level) {
        this.questionChooser.removeAllItems();
        ArrayList<Integer> questionIds = qdao.findByLevel(level);
        this.questionChooser.addItem("Nouvelle question");
        questionIds.forEach((qid) -> {
            Question q = qdao.find(qid);
            String item = q.getId() + " " + q.getQuestion();
            this.questionChooser.addItem(item);
        });
    }

    /* *
     * Initialisation des composants graphiques
     */
    private void initComponents() {
        Dimension textSize = new Dimension(500, 500);
        /* On met des zones dans notre panneau */
        this.setLayout(new BorderLayout());

        /* Des JPanel pour les différentes zones */
        // La zone nord contient les sélecteurs de niveau et de question l'un
        // au-dessus de l'autre avec leur label pour l'occasion
        JPanel north = new JPanel();
        north.setBorder(BorderFactory.createTitledBorder("Choix de la question"));
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        JPanel northLevel = new JPanel(new FlowLayout());
        northLevel.setBorder(BorderFactory.createTitledBorder("Choix du niveau"));
        northLevel.add(new JLabel("Niveau"));
        northLevel.add(this.levelChooser);
        JPanel northQuestion = new JPanel(new FlowLayout());
        northQuestion.setBorder(BorderFactory.createTitledBorder("Choix de la question"));
        northQuestion.add(new JLabel("Question"));
        northQuestion.add(this.questionChooser);
        north.add(northLevel, BorderLayout.NORTH);
        north.add(northQuestion, BorderLayout.NORTH);

        /* Des JScrollPane et JPanel pour les champ texte */
        // L'espace question
        this.questionText.setLineWrap(true);
        this.questionText.setWrapStyleWord(true);
        JScrollPane questionPane = new JScrollPane(this.questionText);
        questionPane.setPreferredSize(textSize);
        questionPane.setBorder(BorderFactory.createTitledBorder("Enoncé"));
        questionPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        questionPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // L'espace réponse
        JPanel reponsePane = new JPanel();
        reponsePane.setBorder(BorderFactory.createTitledBorder("Réponse"));
        reponsePane.add(this.reponseText);


        // La zone centre contient la question et sa réponse l'un au-dessus de
        // l'autre, suivi du choix du niveau
        JPanel questionReponse = new JPanel();
        questionReponse.setLayout(new BoxLayout(questionReponse, BoxLayout.Y_AXIS));
        questionReponse.setBorder(BorderFactory.createTitledBorder("Données de la question"));
        questionReponse.add(questionPane, BorderLayout.CENTER);
        questionReponse.add(reponsePane, BorderLayout.CENTER);
        JPanel levelPane = new JPanel();
        levelPane.setBorder(BorderFactory.createTitledBorder("Niveau"));
        levelPane.add(this.level1);
        levelPane.add(this.level2);
        questionReponse.add(levelPane);


        // La zone sud contient les boutons l'un à côté de l'autre et centrés,
        // et le label de retour d'information dessous
        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        JPanel southButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southButtons.setBorder(BorderFactory.createTitledBorder("Validation"));
        southButtons.add(this.jbOK);
        southButtons.add(this.jbCancel);
        south.add(southButtons);
        // Configuration du label de retour en rouge
        JPanel retourPane = new JPanel();
        retourPane.setBorder(BorderFactory.createTitledBorder("Autres informations"));
        this.retour.setForeground(Color.RED);
        retourPane.add(this.retour);
        south.add(retourPane);

        /* Ajout des JPanel de zone dans les zones */
        this.add(north, BorderLayout.NORTH);
        this.add(questionReponse, BorderLayout.CENTER);
        this.add(south, BorderLayout.SOUTH);
    }

    /**
     * Initialisation des écouteurs
     */
    private void initListeners() {
        // Validation de la questionText modifiée ou création d'une questionText
        this.jbOK.addActionListener((e) -> addOrUpdateQuestion());
        this.jbCancel.addActionListener((e) -> clearFields());
        this.levelChooser.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                changeLevel();
            }
        });
        this.questionChooser.addItemListener((e) -> {
            if ((e.getStateChange() == ItemEvent.SELECTED)
                    && (questionChooser.getSelectedIndex() != 0)) {
                selectQuestion();
            }
        });
    }

    /**
     * Ajout ou mise à jour d'une question dans la BD
     */
    private void addOrUpdateQuestion() {
        Integer niveau = Integer.parseInt(this.radioLevel.getSelection().getActionCommand());
        String ask = this.questionText.getText();
        String response = this.reponseText.getText();
        if (this.questionChooser.getSelectedIndex() == 0) { // Si nouvelle question
            qdao.create(new Question(null, ask, response, niveau)); // on la crée
            this.updateQuestionList(this.level); // et on met la liste à jour
            this.retour.setText("Question ajoutée");
        } else { // Sinon on récupère l'id de la question et on l'update
            this.question.setNiveau(niveau);
            this.question.setQuestion(ask);
            this.question.setReponse(response);
            this.question.setId(getIndexFromComboItem((String) this.questionChooser.getSelectedItem()));
            qdao.update(question);
            this.retour.setText("Mise à jour effectuée");
        }
        this.updateQuestionList(this.level);
        this.clearFields();
    }

    /**
     * Nettoyage des champs question et réponse
     */
    private void clearFields() {
        this.questionText.setText("");
        this.reponseText.setText("");
    }

    /**
     * Choix du niveau des questions à modifier ou ajouter
     */
    private void changeLevel() {
        this.level = (Integer) this.levelChooser.getSelectedItem(); // On met le niveau à jour
        if (this.level == 1) {
            this.level1.setSelected(true);
        } else {
            this.level2.setSelected(true);
        }
        this.updateQuestionList(this.level); // et on met la liste des questions à jour
        this.clearFields();
    }

    /**
     * Sélection d'une question à modifier
     */
    private void selectQuestion() {
        String selectedQuestion = (String) this.questionChooser.getSelectedItem();
        this.question = this.qdao.find(getIndexFromComboItem(selectedQuestion));
        this.questionText.setText(this.question.getQuestion());
        this.reponseText.setText(this.question.getReponse());
    }

    /**
     * Extraction de l'id de la question dans la BD depuis la chaîne de
     * caractères de la liste déroulante.
     *
     * @param s
     * @return
     */
    private Integer getIndexFromComboItem(String s) {
        Integer returnId;
        int spacePosition = s.indexOf(' ');
        returnId = Integer.parseInt(s.substring(0, spacePosition));
        return returnId;
    }
}
