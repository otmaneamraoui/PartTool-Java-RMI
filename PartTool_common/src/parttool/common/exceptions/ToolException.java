package parttool.common.exceptions;

import java.io.Serializable;

/**
 * Exception métier du serveur d'outils (V2).
 *
 * Pourquoi une exception métier ?
 * - RemoteException = problème technique (réseau, registry, sérialisation...)
 * - ToolException   = problème fonctionnel (token invalide, données outil incorrectes, etc.)
 *
 * En RMI, une exception peut remonter au client.
 * On la rend Serializable pour rester propre et éviter des soucis de transport.
 */
public class ToolException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    public ToolException(String message) {
        super(message);
    }

    public ToolException(String message, Throwable cause) {
        super(message, cause);
    }
}
