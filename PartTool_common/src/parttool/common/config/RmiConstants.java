package parttool.common.config;

/**
 * Constantes partagées entre clients et serveurs.
 * - Nom des services RMI
 * - Ports des registries
 */
public final class RmiConstants {

    // ===== Auth (V1) =====
    public static final int AUTH_REGISTRY_PORT = 1099;
    public static final String AUTH_SERVICE_NAME = "AuthService";

    // ===== Tools (V2) =====
    public static final int TOOLS_REGISTRY_PORT = 1100;
    public static final String TOOL_SERVICE_NAME = "ToolService";
    
    // ===== Storage (V3) =====
    public static final int STORAGE_REGISTRY_PORT = 1200;
    public static final String STORAGE_SERVICE_NAME = "StorageService";

    private RmiConstants() {
        // utilitaire
    }
}
