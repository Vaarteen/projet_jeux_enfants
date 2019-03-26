package dao;

public class DAOFactory {

    /**
     * Returns an instance of QuestionDAO
     *
     * @return Une instance de QuestionDAO
     */
    public static QuestionDAO getQuestionDAO() {
        return new QuestionDAO();
    }
}
