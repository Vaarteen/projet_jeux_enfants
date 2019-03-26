package dao;

import java.sql.Connection;

public abstract class DAO<T> {

    protected Connection connection = MySQLConnection.getInstance();

    /**
     * Permet de récupérer un objet via son ID. C'est "l'hydratation"
     *
     * @param id: l'id de l'enregistrement dans la BD
     * @return Un objet hydraté depuis la BD
     */
    public abstract T find(Integer id);

    /**
     * Permet de créer un enregistrement dans la base de données correspondant à
     * l'objet passé en paramètre
     *
     * @param obj: l'objet représentant l'enregistrement à insérer dans la BD
     * @return L'objet réhydraté depuis le BD
     */
    public abstract T create(T obj);

    /**
     * Permet de mettre à jour les données d'un enregistrement dans la BD
     *
     * @param obj: L'objet à modifier dans la BD
     * @return L'objet réhydraté depuis la BD
     */
    public abstract T update(T obj);

    /**
     * Permet la suppression d'une entrée de la BD
     *
     * @param obj: L'objet à supprimer dans la BD
     */
    public abstract void delete(T obj);
}
