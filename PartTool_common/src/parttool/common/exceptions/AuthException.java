package parttool.common.exceptions;

import java.io.Serializable;

/**
 * Exception métier pour l'authentification (ex: mauvais code, carte inconnue, compte désactivé).
 *
 * Pourquoi une exception "métier" ?
 * - RemoteException signale un problème technique (réseau, sérialisation, registry…).
 * - AuthException signale un problème fonctionnel (règles métier).
 *
 * Remarque RMI importante (cours) :
 * - En RMI, les exceptions peuvent être transmises au client.
 * - Pour être transportée facilement, l'exception doit être sérialisable.
 *
 * Ici on fait :
 * - extends Exception : c'est une exception "checked" => le client est obligé de la gérer.
 * - implements Serializable : pour un transport RMI propre.
 */
public class AuthException extends Exception implements Serializable {

    // Bon réflexe : fixer un serialVersionUID pour éviter les soucis de compatibilité
    // si la classe évolue entre compilations.
    private static final long serialVersionUID = 1L;

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
