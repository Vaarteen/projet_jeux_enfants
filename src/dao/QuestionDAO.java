package dao;

import beans.Question;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuestionDAO extends DAO<Question> {

    private static final String TABLE = "questions";

    // Les override
    @Override
    public Question find(Integer id) {
        Question question = null;
        try {
            String req = "SELECT * FROM " + TABLE + " WHERE id = ?";
            PreparedStatement pstmt = this.connection.prepareStatement(req);
            pstmt.setLong(1, id);
            ResultSet result = pstmt.executeQuery();
            if (result.first()) {
                question = new Question(
                        id,
                        result.getString("question"),
                        result.getString("answer"),
                        result.getInt("level")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(QuestionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return question;
    }

    @Override
    public Question create(Question obj) {
        try {
            String req = "INSERT INTO " + TABLE + " (question, answer, level) VALUES(?, ?, ?)";
            PreparedStatement pstmt = this.connection.prepareStatement(
                    req, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, obj.getQuestion());
            pstmt.setString(2, obj.getReponse());
            pstmt.setInt(3, obj.getNiveau());
            // On soumet la requête et on récupère l'id créé
            int id = pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int last_inserted_id;
            if (rs.first()) { // Si on a des id créés
                last_inserted_id = rs.getInt(1);
                // On récupère l'enregistrement créé
                obj = this.find(last_inserted_id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(QuestionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj;
    }

    @Override
    public Question update(Question obj) {
        try {
            String req = "UPDATE " + TABLE + " SET question = ?, reponse = ?, niveau = ?"
                    + " WHERE id = ?";
            PreparedStatement pstmt = this.connection.prepareStatement(req);
            pstmt.setString(1, obj.getQuestion());
            pstmt.setString(2, obj.getReponse());
            pstmt.setInt(3, obj.getNiveau());
            pstmt.setLong(4, obj.getId());
            pstmt.executeUpdate();
            // On récupère l'enregistrement modifié
            obj = this.find(obj.getId());
        } catch (SQLException ex) {
            Logger.getLogger(QuestionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj;
    }

    @Override
    public void delete(Question obj) {
        try {
            String req = "DELETE FROM " + TABLE + " WHERE id = ?";
            PreparedStatement pstmt = this.connection.prepareStatement(req);
            pstmt.setLong(1, obj.getId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(QuestionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Retourne tous les id des questions d'un niveau passé en paramètre
     *
     * @param niveau: le niveau des questions
     * @return Une liste des id des questions dans la BD
     */
    public ArrayList<Integer> findByLevel(int niveau) {
        ArrayList<Integer> questionIds = new ArrayList<>();
        try {
            String req = "SELECT id FROM " + TABLE + " WHERE level <= ?";
            PreparedStatement pstmt = this.connection.prepareStatement(req);
            pstmt.setInt(1, niveau);
            ResultSet result = pstmt.executeQuery();
            while (result.next()) {
                questionIds.add(result.getInt("id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(QuestionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return questionIds;
    }
}
