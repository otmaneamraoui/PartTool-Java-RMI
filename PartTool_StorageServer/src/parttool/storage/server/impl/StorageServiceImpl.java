package parttool.storage.server.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import parttool.common.dto.StorageLocationDTO;
import parttool.common.dto.Token;
import parttool.common.exceptions.AuthException;
import parttool.common.exceptions.StorageException;
import parttool.common.rmi.AuthService;
import parttool.common.rmi.StorageService;
import parttool.storage.server.model.StorageLocation;
import parttool.storage.server.repo.StorageRepository;

/**
 * Implémentation du service RMI de gestion des locaux de stockage.
 *
 * Version améliorée :
 * - le client ne donne plus un token
 * - il donne une carte + un PIN
 * - le serveur Storage contacte le serveur Auth pour authentifier l'usager
 *
 * Cela est plus proche du sujet :
 * l'usager présente sa carte pour ouvrir le local.
 */
public class StorageServiceImpl extends UnicastRemoteObject implements StorageService {

    private static final long serialVersionUID = 1L; // pour éviter un warning de sérialisation

    private final StorageRepository repository;
    private final AuthService authService;

    // Le constructeur reçoit les dépendances (repository + service Auth)
    public StorageServiceImpl(StorageRepository repository, AuthService authService) throws RemoteException {
        super();
        this.repository = repository;
        this.authService = authService;
    }

    // Le client ne fournit plus de token, mais une carte + un PIN
    @Override
    public StorageLocationDTO openStorage(int storageId, int cardId, String pin)
            throws RemoteException, StorageException {

        // 1) vérifier que l'utilisateur peut bien se connecter via AuthServer
        Token token = authenticate(cardId, pin);

        // 2) vérifier que le local existe
        StorageLocation location = repository.findById(storageId);
        if (location == null) {
            throw new StorageException("Local introuvable : " + storageId);
        }

        // 3) ouvrir le local
        location.setOpen(true);
        repository.save(location);

        // 4) retourner le DTO du local
        return toDTO(location);
    }

    // Même logique que openStorage, mais sans ouvrir le local
    @Override
    public boolean validateAccess(int storageId, int cardId, String pin)
            throws RemoteException, StorageException {

        // 1) vérifier que l'authentification marche
        authenticate(cardId, pin);

        // 2) vérifier que le local existe
        StorageLocation location = repository.findById(storageId);
        if (location == null) {
            throw new StorageException("Local introuvable : " + storageId);
        }

        return true;
    }

    /**
     * Demande au serveur Auth d'authentifier l'utilisateur.
     *
     * Si le login échoue :
     * - AuthServer lève AuthException
     * - on la transforme ici en StorageException
     *
     * Si le login réussit :
     * - on récupère un Token
     * - on pourrait le stocker plus tard si besoin (V4)
     */
    private Token authenticate(int cardId, String pin) throws StorageException {
        try {
            return authService.login(cardId, pin);
        } catch (AuthException e) {
            throw new StorageException("Accès refusé : carte ou PIN invalide", e);
        } catch (RemoteException e) {
            throw new StorageException("Impossible de contacter le serveur Auth", e);
        }
    }

    /**
     * Convertit le modèle interne en DTO transportable.
     */
    private StorageLocationDTO toDTO(StorageLocation location) {
        return new StorageLocationDTO(
                location.getId(), 
                location.getName(),
                location.isOpen()
        );
    }
}