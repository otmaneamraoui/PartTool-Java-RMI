package parttool.common.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import parttool.common.dto.StorageLocationDTO;
import parttool.common.exceptions.StorageException;

/**
 * CONTRAT RMI du serveur de local de stockage (V3 amélioré).
 *
 * Nouveau choix :
 * - il fournit une carte + un PIN
 * - le serveur Storage contacte ensuite AuthServer pour authentifier l'usager
 */
public interface StorageService extends Remote {

    /**
     * Tente d'ouvrir un local pour un usager identifié par sa carte et son PIN.
     *
     * @param storageId identifiant du local
     * @param cardId identifiant de la carte
     * @param pin code confidentiel
     * @return DTO du local ouvert
     */
    StorageLocationDTO openStorage(int storageId, int cardId, String pin)
            throws RemoteException, StorageException;

    /**
     * Vérifie si une carte + PIN permettent d'accéder à un local.
     *
     * @param storageId identifiant du local
     * @param cardId identifiant de la carte
     * @param pin code confidentiel
     * @return true si accès autorisé
     */
    boolean validateAccess(int storageId, int cardId, String pin)
            throws RemoteException, StorageException;
}