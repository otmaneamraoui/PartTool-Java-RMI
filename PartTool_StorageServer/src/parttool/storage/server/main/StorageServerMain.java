package parttool.storage.server.main;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import parttool.common.config.RmiConstants;
import parttool.common.rmi.AuthService;
import parttool.common.rmi.StorageService;
import parttool.storage.server.impl.StorageServiceImpl;
import parttool.storage.server.model.StorageLocation;
import parttool.storage.server.repo.InMemoryStorageRepository;
import parttool.storage.server.repo.StorageRepository;
import parttool.storage.server.rmi.AuthServiceConnector;

/**
 * Point d'entrée du serveur de local de stockage (V3).
 *
 * Rôle :
 * - démarrer le registry RMI du serveur Storage
 * - se connecter au serveur Auth
 * - initialiser quelques locaux
 * - publier le service StorageService
 */
public class StorageServerMain {

    public static void main(String[] args) {
        try {
            // 1) Démarrer le registry RMI du serveur Storage
            LocateRegistry.createRegistry(RmiConstants.STORAGE_REGISTRY_PORT);
            System.out.println("[StorageServer] RMI registry démarré sur le port " +
                    RmiConstants.STORAGE_REGISTRY_PORT);

            // 2) Se connecter au serveur Auth
            AuthService authService = new AuthServiceConnector("localhost").connect();
            System.out.println("[StorageServer] Connecté au serveur Auth.");

            // 3) Créer le repository et initialiser quelques locaux
            StorageRepository repository = new InMemoryStorageRepository();

            // Initialiser quelques locaux de stockage
            repository.save(new StorageLocation(1, "Local A", false));
            repository.save(new StorageLocation(2, "Local B", false));
            repository.save(new StorageLocation(3, "Local C", false));

            // 4) Créer le service
            StorageService service = new StorageServiceImpl(repository, authService);

            // 5) Publier le service dans le registry Storage
            String url = "rmi://localhost:" +
                    RmiConstants.STORAGE_REGISTRY_PORT + "/" +
                    RmiConstants.STORAGE_SERVICE_NAME;

            // Si un service est déjà publié à cette URL, le remplacer
            Naming.rebind(url, service);

            System.out.println("[StorageServer] Service publié : " + url);
            System.out.println("[StorageServer] Serveur prêt.");

        } catch (Exception e) {
            System.err.println("[StorageServer] ERREUR serveur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}