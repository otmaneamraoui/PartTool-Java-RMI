package parttool.client.storage.main;

import java.rmi.Naming;
import java.util.Scanner;

import parttool.common.config.RmiConstants;
import parttool.common.dto.StorageLocationDTO;
import parttool.common.exceptions.StorageException;
import parttool.common.rmi.StorageService;

/**
 * Client du terminal du local de stockage.
 *
 * Version améliorée :
 * - l'utilisateur ne saisit plus un token
 * - il saisit son numéro de carte + son PIN
 *
 * Le client appelle ensuite le serveur Storage,
 * qui lui-même contacte le serveur Auth pour vérifier l'identité.
 */
public class StorageClientMain {

    public static void main(String[] args) {

        try {

            // 1) Connexion au serveur Storage
            String url = "rmi://localhost:" +
                    RmiConstants.STORAGE_REGISTRY_PORT + "/" +
                    RmiConstants.STORAGE_SERVICE_NAME;

            System.out.println("[StorageClient] Lookup : " + url);

            // Le client agit comme client RMI du serveur Storage
            StorageService service = (StorageService) Naming.lookup(url);

            System.out.println("[StorageClient] Connecté au serveur Storage.");

            Scanner sc = new Scanner(System.in);

            while (true) {

                System.out.println("\n===== TERMINAL LOCAL =====");
                System.out.println("1) Ouvrir un local");
                System.out.println("0) Quitter");
                System.out.print("Choix : ");

                String choice = sc.nextLine();

                if ("0".equals(choice)) {
                    System.out.println("Fin du client.");
                    break;
                }

                if ("1".equals(choice)) {

                    System.out.print("ID du local : ");
                    int storageId = Integer.parseInt(sc.nextLine());

                    System.out.print("Numéro de carte : ");
                    int cardId = Integer.parseInt(sc.nextLine());

                    System.out.print("PIN : ");
                    String pin = sc.nextLine();

                    try {
                    	// pour ouvrir un local, le client fournit une carte + un PIN
                        StorageLocationDTO location = service.openStorage(storageId, cardId, pin);

                        System.out.println("Local ouvert : " + location);

                    } catch (StorageException e) {

                        System.out.println("Accès refusé : " + e.getMessage());

                    }

                }

            }

            sc.close();

        } catch (Exception e) {

            System.err.println("[StorageClient] Erreur : " + e.getMessage());
            e.printStackTrace();

        }

    }
}