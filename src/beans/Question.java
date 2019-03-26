package beans;

public class Question {

    /* Les variables d'instance qui correspondent aux colonnes de la table dans
       la BD */
    private Integer id;
    private String question;
    private String reponse;
    private Integer niveau;

    /*
     * Constructeur
     */
    public Question(Integer id, String question, String reponse, Integer niveau) {
        this.id = id;
        this.question = question;
        this.reponse = reponse;
        this.niveau = niveau;
    }

    /**
     * Accesseur de la variable id
     *
     * @return L'id dans la BD de l'objet
     */
    public Integer getId() {
        return id;
    }

    /**
     * bla
     *
     * @param id bli
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Accesseur de la variable question
     *
     * @return Le texte de la question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Mutateur de la variable question
     *
     * @param question Le texte de la question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Accesseur de la variable reponse
     *
     * @return Le texte de la réponse
     */
    public String getReponse() {
        return reponse;
    }

    /**
     * Mutateur de la variable reponse
     *
     * @param reponse Le texte de la réponse
     */
    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    /**
     * Accesseur de la variable niveau
     *
     * @return Le niveau de la question
     */
    public Integer getNiveau() {
        return niveau;
    }

    /**
     * Mutateur de la variable niveau
     *
     * @param niveau Le niveau de la question
     */
    public void setNiveau(Integer niveau) {
        this.niveau = niveau;
    }


}
