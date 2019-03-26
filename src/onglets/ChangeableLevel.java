
package onglets;

/**
 *
 * @author Herbert Caffarel <herbert.caffarel@cicef.pro>
 */
public interface ChangeableLevel {

    /**
     * Modification du niveau. Doit changer l'affichage.
     *
     * @param niveau Le niveau de la question
     */
    public void changeLevel(int niveau);
}
