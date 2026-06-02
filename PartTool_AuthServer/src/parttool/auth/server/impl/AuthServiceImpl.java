package parttool.auth.server.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import parttool.auth.server.model.User;
import parttool.auth.server.model.UserStatus;
import parttool.auth.server.repo.UserRepository;
import parttool.auth.server.security.PinHasher;
import parttool.auth.server.security.TokenManager;
import parttool.common.dto.Token;
import parttool.common.exceptions.AuthException;
import parttool.common.rmi.AuthService;

/**
 * Implantation (le "COMMENT") du service d'authentification.
 *
 */

// permet d'implémenter une interface RMI (AuthService) et d'être exporté comme objet distant (UnicastRemoteObject)
public class AuthServiceImpl extends UnicastRemoteObject implements AuthService {
	
    private static final long serialVersionUID = 1L;
    
    // repo pour gérer les utilisateurs (stockage, recherche, etc.)
    private final UserRepository userRepo;
    // gestionnaire de tokens pour créer/valider/invalider les tokens d'authentification
    private final TokenManager tokenManager;

    // Constructeur : reçoit les dépendances (repo + token manager) et les stocke dans des champs
    public AuthServiceImpl(UserRepository userRepo, TokenManager tokenManager) throws RemoteException {
        super(); // Appel du constructeur parent (UnicastRemoteObject) pour exporter l'objet distant
        this.userRepo = userRepo;
        this.tokenManager = tokenManager;
    }

    // Implémentation des méthodes de l'interface AuthService
    @Override
    public boolean signup(int cardId, String pin, String fullName, String email)
            throws RemoteException, AuthException {

        // Contrôles simples V1
        if (cardId <= 0) throw new AuthException(" => cardId invalide");
        if (pin == null || pin.length() < 4) throw new AuthException(" => PIN invalide (min 4 caractères)");

        // Vérification d'existence de la carte (cardId doit être unique)
        if (userRepo.exists(cardId)) {
            // Choix V1 : on lève une exception métier claire
            throw new AuthException(" => Carte déjà inscrite : " + cardId);
        }

        // on stocke le hash du PIN (jamais le PIN en clair !) et on crée un nouvel utilisateur actif
        String pinHash = PinHasher.sha256(pin);
        User user = new User(cardId, pinHash, fullName, email, UserStatus.ACTIF);
        userRepo.save(user);

        return true;
    }

    // Contrôles d'authentification V1 : existence de la carte, statut actif, correspondance du PIN
    @Override
    public Token login(int cardId, String pin) throws RemoteException, AuthException {
        if (pin == null) throw new AuthException(" => PIN manquant");

        User user = userRepo.findByCardId(cardId);
        if (user == null) throw new AuthException(" => Carte inconnue : " + cardId);

        if (user.getStatus() != UserStatus.ACTIF) {
            throw new AuthException(" => Compte désactivé");
        }

        String pinHash = PinHasher.sha256(pin);
        if (!pinHash.equals(user.getPinHash())) {
            throw new AuthException(" => PIN incorrect");
        }

        // OK : on génère un token
        return tokenManager.createToken(cardId);
    }

    // Validation d'un token : on délègue au TokenManager qui gère la logique de validité (expiration, etc.)
    @Override
    public boolean validate(Token token) throws RemoteException {
        // Ici pas d'exception métier : validate renvoie true/false
        return tokenManager.validate(token);
    }

    // Invalidation d'un token (logout) : on délègue au TokenManager
    @Override
    public void logout(Token token) throws RemoteException {
        tokenManager.invalidate(token);
    }
}
