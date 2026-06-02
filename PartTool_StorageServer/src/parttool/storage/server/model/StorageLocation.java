package parttool.storage.server.model;

/**
 * Modèle interne représentant un local de stockage côté serveur.
 *
 * Différence avec StorageLocationDTO :
 * - DTO : transport de données entre client et serveur
 * - modèle : état réel manipulé par le serveur
 */

public class StorageLocation {

    private final int id;
    private final String name;

    // état du local (ouvert / fermé)
    private boolean open;

    public StorageLocation(int id, String name, boolean open) {
        this.id = id;
        this.name = name;
        this.open = open;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}