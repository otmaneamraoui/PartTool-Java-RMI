package parttool.tools.server.rmi;

import java.rmi.Naming;

import parttool.common.config.RmiConstants;
import parttool.common.rmi.AuthService;

/**
 * Connecteur RMI côté serveur Tools vers le serveur Auth (V1).
 *
 * Pourquoi un connecteur ?
 * - Le ToolsServer est lui-même un "client RMI" du AuthServer.
 * - On isole les détails réseau (URL, lookup) pour garder ToolServiceImpl propre.
 */
public class AuthServiceConnector {

    private final String authHost;

    public AuthServiceConnector(String authHost) {
        this.authHost = authHost;
    }

    /**
     * Récupère le stub AuthService via le RMI registry de l'auth.
     */
    public AuthService connect() throws Exception {
        String url = "rmi://" + authHost + ":" + RmiConstants.AUTH_REGISTRY_PORT + "/" + RmiConstants.AUTH_SERVICE_NAME;
        System.out.println("[ToolsServer] Lookup AuthService : " + url);
        return (AuthService) Naming.lookup(url);
    }
}
