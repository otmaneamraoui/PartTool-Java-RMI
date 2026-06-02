package parttool.tools.server.main;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import parttool.common.config.RmiConstants;
import parttool.common.rmi.AuthService;
import parttool.common.rmi.ToolService;
import parttool.tools.server.impl.ToolServiceImpl;
import parttool.tools.server.repo.InMemoryToolRepository;
import parttool.tools.server.repo.ToolRepository;
import parttool.tools.server.rmi.AuthServiceConnector;
import parttool.tools.server.util.QrIdGenerator;

/**
 * Point d'entrée du serveur Tools (V2).
 *
 * Rôle :
 * 1) Démarrer le RMI registry du serveur Tools (port 1100)
 * 2) Se connecter au serveur Auth (lookup AuthService sur port 1099)
 * 3) Instancier ToolServiceImpl
 * 4) Publier ToolService dans le registry Tools
 *
 * Important :
 * - Le ToolsServer dépend du AuthServer car il doit vérifier les tokens.
 * - Donc : démarre d'abord AuthServer, puis ToolsServer.
 */
public class ToolsServerMain {

    public static void main(String[] args) {
        try {
            // 1) Démarrage du registry Tools
            LocateRegistry.createRegistry(RmiConstants.TOOLS_REGISTRY_PORT);
            System.out.println("[ToolsServer] RMI registry démarré sur le port " + RmiConstants.TOOLS_REGISTRY_PORT);

            // 2) Connexion au serveur Auth (ToolsServer agit comme client RMI du AuthServer)
            AuthService auth = new AuthServiceConnector("localhost").connect();
            System.out.println("[ToolsServer] Connecté au serveur Auth.");

            // 3) Dépendances serveur (stockage + génération d'IDs)
            ToolRepository repo = new InMemoryToolRepository();
            QrIdGenerator idGen = new QrIdGenerator();

            // 4) Implémentation du service Tools
            ToolService service = new ToolServiceImpl(repo, idGen, auth);

            // 5) Publication ToolService dans le registry Tools
            String url = "rmi://localhost:" + RmiConstants.TOOLS_REGISTRY_PORT + "/" + RmiConstants.TOOL_SERVICE_NAME;
            Naming.rebind(url, service);

            System.out.println("[ToolsServer] Service publié : " + url);
            System.out.println("[ToolsServer] Serveur prêt.");

        } catch (Exception e) {
            System.err.println("[ToolsServer] ERREUR serveur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
