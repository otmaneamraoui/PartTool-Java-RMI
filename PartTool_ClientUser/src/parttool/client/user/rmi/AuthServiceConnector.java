package parttool.client.user.rmi;

import java.rmi.Naming;

import parttool.common.config.RmiConstants;
import parttool.common.rmi.AuthService;

/**
 * Classe utilitaire dédiée à la connexion RMI au serveur d'authentification.
 *
 * Pourquoi la séparer ?
 * - Le menu console ne doit pas connaître les détails réseau (URL, lookup…).
 * - En V2/V3, on fera pareil pour ToolService, StorageService, etc.
 *
 * RMI (cours) :
 * - lookup => récupère une référence distante (stub/proxy) implémentant l'interface AuthService.
 */
public class AuthServiceConnector {

    private final String host;

    public AuthServiceConnector(String host) {
        this.host = host;
    }

    /**
     * Retourne le stub AuthService via Naming.lookup.
     */
    public AuthService connect() throws Exception {
    	String url = "rmi://" + host + ":" + RmiConstants.AUTH_REGISTRY_PORT + "/" + RmiConstants.AUTH_SERVICE_NAME;
        System.out.println("[Client] Lookup : " + url);
        return (AuthService) Naming.lookup(url);
    }
}
