package parttool.client.user.ui;

import java.util.List;
import java.util.Scanner;

import parttool.client.user.rmi.AuthServiceConnector;
import parttool.client.user.rmi.ToolServiceConnector;
import parttool.common.dto.Token;
import parttool.common.dto.ToolCategory;
import parttool.common.dto.ToolCreatedDTO;
import parttool.common.dto.ToolDTO;
import parttool.common.dto.ToolSummaryDTO;
import parttool.common.exceptions.AuthException;
import parttool.common.exceptions.ToolException;
import parttool.common.rmi.AuthService;
import parttool.common.rmi.ToolService;

/**
 * Menu console client.
 *
 * V1 :
 * - Signup / Login / Validate / Logout via AuthService
 *
 * V2 :
 * - Déclarer un outil via ToolService (serveur Tools)
 * - L'outil est déclaré uniquement si le token est valide (vérifié côté serveur Tools)
 */
public class ConsoleMenu {

    private final Scanner sc = new Scanner(System.in);

    // Stubs RMI
    private AuthService auth;
    private ToolService tools;

    // Token courant (session)
    private Token currentToken;

    public void run() {
        try {
            // Connexion au serveur Auth (V1)
            auth = new AuthServiceConnector("localhost").connect();
            System.out.println("[Client] Connecté au service d'auth.");

            // Connexion au serveur Tools (V2)
            tools = new ToolServiceConnector("localhost").connect();
            System.out.println("[Client] Connecté au service Tools.");

            loopMenu();
        } catch (Exception e) {
            System.out.println("[Client] Erreur : " + e.getMessage());
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }

    private void loopMenu() {
        while (true) {
            System.out.println("\n====== PART'TOOL (V1+V2) ======");
            System.out.println("1) Signup (inscription)");
            System.out.println("2) Login (connexion)");
            System.out.println("3) Validate token");
            System.out.println("4) Logout");
            System.out.println("5) Déclarer un outil (V2)");
            System.out.println("6) Lister mes outils (V2)");
            System.out.println("0) Quitter");
            System.out.print("Choix: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    doSignup();
                    break;
                case "2":
                    doLogin();
                    break;
                case "3":
                    doValidate();
                    break;
                case "4":
                    doLogout();
                    break;
                case "5":
                    doDeclareTool();
                    break;
                case "6":
                    doListMyTools();
                    break;
                case "0":
                    System.out.println("[Client] Au revoir.");
                    return;
                default:
                    System.out.println("[Client] Choix invalide.");
            }
        }
    }

    private void doSignup() {
        try {
            System.out.print("cardId (int): ");
            int cardId = Integer.parseInt(sc.nextLine().trim());

            System.out.print("PIN (min 4 chars): ");
            String pin = sc.nextLine().trim();

            System.out.print("Nom complet: ");
            String fullName = sc.nextLine().trim();

            System.out.print("Email: ");
            String email = sc.nextLine().trim();

            boolean ok = auth.signup(cardId, pin, fullName, email);
            System.out.println(ok ? "[Client] Inscription OK." : "[Client] Inscription refusée.");

        } catch (AuthException ae) {
            System.out.println("[Client] Erreur métier : " + ae.getMessage());
        } catch (Exception e) {
            System.out.println("[Client] Erreur technique : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void doLogin() {
        try {
            System.out.print("cardId (int): ");
            int cardId = Integer.parseInt(sc.nextLine().trim());

            System.out.print("PIN: ");
            String pin = sc.nextLine().trim();

            currentToken = auth.login(cardId, pin);
            System.out.println("[Client] Login OK. Token = " + currentToken);

        } catch (AuthException ae) {
            System.out.println("[Client] Erreur métier : " + ae.getMessage());
        } catch (Exception e) {
            System.out.println("[Client] Erreur technique : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void doValidate() {
        try {
            if (currentToken == null) {
                System.out.println("[Client] Aucun token. Faites d'abord login.");
                return;
            }

            boolean valid = auth.validate(currentToken);
            System.out.println(valid ? "[Client] Token VALIDE." : "[Client] Token INVALIDE.");

        } catch (Exception e) {
            System.out.println("[Client] Erreur technique : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void doLogout() {
        try {
            if (currentToken == null) {
                System.out.println("[Client] Pas connecté.");
                return;
            }

            auth.logout(currentToken);
            currentToken = null;
            System.out.println("[Client] Logout OK.");

        } catch (Exception e) {
            System.out.println("[Client] Erreur technique : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * V2 : Déclaration d'un outil.
     * Nécessite d'être connecté (currentToken non null).
     */
    private void doDeclareTool() {
        try {
            if (currentToken == null) {
                System.out.println("[Client] Vous devez d'abord vous connecter (login).");
                return;
            }

            System.out.print("Nom outil: ");
            String name = sc.nextLine().trim();

            System.out.print("Description: ");
            String desc = sc.nextLine().trim();

            ToolCategory cat = askCategory();

            System.out.print("Poids (kg): ");
            double weight = Double.parseDouble(sc.nextLine().trim());

            System.out.print("Longueur (cm): ");
            double length = Double.parseDouble(sc.nextLine().trim());

            System.out.print("Largeur (cm): ");
            double width = Double.parseDouble(sc.nextLine().trim());

            System.out.print("Hauteur (cm): ");
            double height = Double.parseDouble(sc.nextLine().trim());

            ToolDTO dto = new ToolDTO(name, desc, cat, weight, length, width, height);

            // Appel distant au serveur Tools
            ToolCreatedDTO created = tools.declareTool(currentToken, dto);

            System.out.println("[Client] Outil déclaré !");
            System.out.println("  toolId = " + created.getToolId());
            System.out.println("  qrId   = " + created.getQrId());
            System.out.println("  msg    = " + created.getMessage());

        } catch (ToolException te) {
            System.out.println("[Client] Erreur métier (Tools) : " + te.getMessage());
        } catch (Exception e) {
            System.out.println("[Client] Erreur technique : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * V2 : Affiche les outils de l'utilisateur connecté.
     */
    private void doListMyTools() {
        try {
            if (currentToken == null) {
                System.out.println("[Client] Vous devez d'abord vous connecter (login).");
                return;
            }

            List<ToolSummaryDTO> list = tools.getMyTools(currentToken);

            if (list.isEmpty()) {
                System.out.println("[Client] Aucun outil déclaré.");
                return;
            }

            System.out.println("[Client] Mes outils :");
            for (ToolSummaryDTO t : list) {
                System.out.println(" - " + t);
            }

        } catch (ToolException te) {
            System.out.println("[Client] Erreur métier (Tools) : " + te.getMessage());
        } catch (Exception e) {
            System.out.println("[Client] Erreur technique : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Petit helper : demande une catégorie parmi les valeurs de l'enum.
     * (ça évite que l'utilisateur tape n'importe quoi)
     */
    private ToolCategory askCategory() {
        while (true) {
            System.out.println("Catégorie :");
            System.out.println(" 1) BRICOLAGE");
            System.out.println(" 2) JARDINAGE");
            System.out.println(" 3) AUTOMOBILE");
            System.out.println(" 4) AUTRE");
            System.out.print("Choix catégorie: ");

            String c = sc.nextLine().trim();
            switch (c) {
                case "1": return ToolCategory.BRICOLAGE;
                case "2": return ToolCategory.JARDINAGE;
                case "3": return ToolCategory.AUTOMOBILE;
                case "4": return ToolCategory.AUTRE;
                default:
                    System.out.println("[Client] Choix invalide.");
            }
        }
    }
}
