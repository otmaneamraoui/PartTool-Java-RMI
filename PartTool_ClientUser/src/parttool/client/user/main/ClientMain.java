package parttool.client.user.main;

import parttool.client.user.ui.ConsoleMenu;

/**
 * Point d'entrée du client.
 * Lance simplement le menu console.
 */
public class ClientMain {

    public static void main(String[] args) {
        new ConsoleMenu().run();
    }
}
