package parttool.common.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import parttool.common.dto.Token;
import parttool.common.exceptions.AuthException;

/**
 * CONTRAT RMI (interface distante) : c'est le "QUOI" accessible à distance.
 *
 * Rappels du cours RMI :
 * 1) Toute interface distante doit "extends Remote".
 * 2) Toute méthode distante doit déclarer "throws RemoteException".
 * 3) Les paramètres / retours doivent être :
 *    - types primitifs (int, boolean, ...) OU
 *    - objets Serializable (Token, DTO...) OU
 *    - objets distants (Remote).
 *
 * Important pour ton projet :
 * - V1 : inscription + connexion + renvoi d'un Token
 * - V2/V3 : validate(token) sera utilisé par d'autres serveurs (Tools/Storage) pour vérifier la session.
 */
public interface AuthService extends Remote {

    /**
     * Inscription d'un usager.
     *
     * Choix simple pour la V1 :
     * - Le "cardId" simule le numéro de carte d'accès.
     * - Le "pin" est le code confidentiel.
     *
     * Retour :
     * - boolean : true si inscrit, false si déjà existant (ou tu peux lever AuthException).
     *
     * Exceptions :
     * - RemoteException : problème technique (réseau, registry, sérialisation…)
     * - AuthException : problème métier (format pin invalide, etc.)
     */
    boolean signup(int cardId, String pin, String fullName, String email)
            throws RemoteException, AuthException;

    /**
     * Connexion : vérifie (cardId, pin) et renvoie un Token si OK.
     *
     * - Token est Serializable donc transportable par RMI.
     * - En cas de mauvais code ou carte inconnue : AuthException.
     */
    Token login(int cardId, String pin)
            throws RemoteException, AuthException;

    /**
     * Vérifie si un token est valide (non expiré, connu du serveur, etc.).
     * Cette méthode servira dès la V2 pour sécuriser l'accès au serveur d'outils.
     */
    boolean validate(Token token)
            throws RemoteException;

    /**
     * Déconnexion : invalide le token côté serveur.
     */
    void logout(Token token)
            throws RemoteException;
}
