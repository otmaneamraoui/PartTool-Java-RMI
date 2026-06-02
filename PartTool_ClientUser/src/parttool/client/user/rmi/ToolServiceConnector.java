package parttool.client.user.rmi;

import java.rmi.Naming;

import parttool.common.config.RmiConstants;
import parttool.common.rmi.ToolService;

/**
 * Connecteur RMI côté client vers le serveur Tools (V2).
 *
 * Rôle :
 * - construire l'URL RMI du ToolService
 * - faire le Naming.lookup(...)
 * - retourner le stub ToolService
 *
 * Intérêt :
 * - le menu console n'a pas à connaître l'URL et les détails réseau.
 */
public class ToolServiceConnector {

    private final String host;

    public ToolServiceConnector(String host) {
        this.host = host;
    }

    public ToolService connect() throws Exception {
        String url = "rmi://" + host + ":" + RmiConstants.TOOLS_REGISTRY_PORT + "/" + RmiConstants.TOOL_SERVICE_NAME;
        System.out.println("[Client] Lookup ToolService : " + url);
        return (ToolService) Naming.lookup(url);
    }
}
