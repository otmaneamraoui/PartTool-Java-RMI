package parttool.common.dto;

import java.io.Serializable;

/**
 * DTO représentant un local de stockage.
 *
 * DTO = Data Transfer Object
 * → objet utilisé uniquement pour transporter des données
 *   entre client et serveur via RMI.
 *
 * Pourquoi un DTO ?
 * - éviter d'exposer les classes internes du serveur
 * - séparer la logique métier du transport de données
 */
public class StorageLocationDTO implements Serializable {

    private static final long serialVersionUID = 1L; 

    // identifiant unique du local
    private final int id;

    // nom du local (ex: "Local A", "Local Outils Jardin")
    private final String name;

    // indique si le local est actuellement ouvert
    private final boolean open;

    // constructeur
    public StorageLocationDTO(int id, String name, boolean open) {
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

    @Override
    public String toString() {
        return "StorageLocationDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", open=" + open +
                '}';
    }
}