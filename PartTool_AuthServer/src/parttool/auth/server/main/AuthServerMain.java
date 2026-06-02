package parttool.auth.server.main;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import parttool.auth.server.impl.AuthServiceImpl;
import parttool.auth.server.repo.InMemoryUserRepository;
import parttool.auth.server.repo.UserRepository;
import parttool.auth.server.security.TokenManager;
import parttool.common.config.RmiConstants;
import parttool.common.rmi.AuthService;

/**
 * Point d'entrée du serveur d'authentification.
 *
 * Rôle :
 * 1) Démarrer le RMI registry (sert à publier les services)
 * 2) Instancier l'implantation du service (AuthServiceImpl)
 * 3) Enregistrer l'objet distant dans l'annuaire (Naming.rebind)
 *
 */
public class AuthServerMain {

    public static void main(String[] args) {
        try {
            // 1) Démarrage du registry sur le port 1099 (si pas déjà démarré)
        	LocateRegistry.createRegistry(RmiConstants.AUTH_REGISTRY_PORT);
        	System.out.println("[AuthServer] RMI registry démarré sur le port " + RmiConstants.AUTH_REGISTRY_PORT);

            // 2) Création des dépendances (stockage en mémoire + gestion tokens)
            UserRepository repo = new InMemoryUserRepository();

            // Token TTL : 30 minutes (en ms) 
            long ttlMillis = 30L * 60L * 1000L;
            TokenManager tokenManager = new TokenManager(ttlMillis); 

            // 3) Instanciation du service distant
            AuthService service = new AuthServiceImpl(repo, tokenManager);

            // 4) Enregistrement dans l'annuaire
            String bindName = RmiConstants.AUTH_SERVICE_NAME;
            Naming.rebind(bindName, service);

            System.out.println("[AuthServer] Service publié : " + bindName);
            System.out.println("[AuthServer] Serveur prêt.");

        } catch (Exception e) {
            System.err.println("[AuthServer] ERREUR serveur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
