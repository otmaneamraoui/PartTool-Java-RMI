package parttool.storage.server.rmi;

import java.rmi.Naming;

import parttool.common.config.RmiConstants;
import parttool.common.rmi.AuthService;

/**
 * Connecteur RMI entre le serveur de local de stockage
 * et le serveur d'authentification.
 *
 * Pourquoi cette classe ?
 * - Le serveur Storage est lui-même client du serveur Auth.
 * - On évite de mettre les détails réseau dans StorageServiceImpl.
 * - Cela rend le code plus propre et plus réutilisable.
 */

public class AuthServiceConnector {

	// Adresse du serveur Auth (ex: "localhost")
    private final String authHost;

    // Constructeur qui prend l'adresse du serveur Auth
    public AuthServiceConnector(String authHost) {
        this.authHost = authHost;
    }

    /**
     * Effectue le lookup RMI vers le serveur Auth
     * et retourne le stub AuthService (le proxy local pour le service distant).
     */
    public AuthService connect() throws Exception {
        String url = "rmi://" + authHost + ":" +
                RmiConstants.AUTH_REGISTRY_PORT + "/" +
                RmiConstants.AUTH_SERVICE_NAME;

        System.out.println("[StorageServer] Lookup AuthService : " + url);
        
        // retourne le stub AuthService obtenu via RMI
        return (AuthService) Naming.lookup(url);
    }
}