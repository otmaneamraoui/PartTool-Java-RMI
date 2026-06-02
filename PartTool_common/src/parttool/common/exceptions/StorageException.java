package parttool.common.exceptions;

import java.io.Serializable;

/**
 * Exception métier du serveur de local de stockage (V3).
 *
 * Pourquoi cette exception ?
 * - RemoteException sert aux erreurs techniques RMI
 *   (réseau, registry, communication distante, etc.)
 * - StorageException sert aux erreurs fonctionnelles / métier
 *   (carte inconnue, accès refusé, local indisponible...)
 *
 * En RMI, les exceptions peuvent remonter du serveur vers le client.
 * On la rend Serializable pour rester cohérent avec les objets échangés.
 */
public class StorageException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L; // pour la sérialisation 

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}