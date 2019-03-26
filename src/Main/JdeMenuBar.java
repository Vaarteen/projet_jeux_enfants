package Main;

import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class JdeMenuBar extends JMenuBar {
    // Les entrées de menu
    JMenu activiteMenu,
            niveauMenu,
            adminMenu;

    // Les items de menu
    JMenuItem dessinMenuItem,
            calculMenuItem,
            questionMenuItem,
            niv1MenuItem,
            niv2MenuItem,
            ajoutQuestionMenuItem,
            modifAdminPasswordMenuItem;

    /*
     *Constructeur
     */
    public JdeMenuBar() {
        /* Instanciation des entrées de menu */
        activiteMenu = new JMenu("Activités");
        niveauMenu = new JMenu("Niveau");
        adminMenu = new JMenu("Administration");

        /* Instanciation des items de menu */
        dessinMenuItem = new JMenuItem("Dessin");
        calculMenuItem = new JMenuItem("Calcul");
        questionMenuItem = new JMenuItem("Question");
        niv1MenuItem = new JMenuItem("Niveau 1 (5-7 ans");
        niv2MenuItem = new JMenuItem("Niveau 2 (7-10 ans)");
        ajoutQuestionMenuItem = new JMenuItem("Ajouter/Modifier une question");
        modifAdminPasswordMenuItem = new JMenuItem("Modifier le mot de passe");

        /* Les mnémoniques (les raccourcis ALT+mnemonic pour ouvrir le menu) */
        activiteMenu.setMnemonic('A');
        niveauMenu.setMnemonic('N');
        adminMenu.setMnemonic('D');

        /* Les accélérateurs des items de menu (les raccourcis claviers CTRL+...) */
        dessinMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK));
        calculMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        questionMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        niv1MenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.CTRL_DOWN_MASK));
        niv2MenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, KeyEvent.CTRL_DOWN_MASK));
        ajoutQuestionMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        modifAdminPasswordMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));

        /* Ajout des items de menu aux menus adequats */
        activiteMenu.add(dessinMenuItem);
        activiteMenu.add(calculMenuItem);
        activiteMenu.add(questionMenuItem);
        niveauMenu.add(niv1MenuItem);
        niveauMenu.add(niv2MenuItem);
        adminMenu.add(ajoutQuestionMenuItem);
        adminMenu.add(modifAdminPasswordMenuItem);

        /* Ajout du menu light, ie sans la partie administration
         * On n'ajoute pas le menu d'administration, ce sera fait si le mot de
         * passe de l'administrateur a été trouvé par l'application.
         */
        this.add(activiteMenu);
        this.add(niveauMenu);
    }

    /**
     * Ajout du menu d'administration à la demande
     */
    public void addAdminMenu() {
        this.add(adminMenu);
    }

}
