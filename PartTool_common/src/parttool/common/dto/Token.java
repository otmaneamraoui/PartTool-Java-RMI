package parttool.common.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * Token de session (jeton) renvoyé par le serveur d'authentification après un login réussi.
 *
 * Lien direct avec le sujet :
 * - V1 : le serveur renvoie un "jeton" (numérique ou autre) lors de la connexion.
 * - V2/V3 : les autres serveurs devront vérifier ce jeton via le serveur d'auth.
 *
 * Remarque RMI (cours) :
 * - Tout objet envoyé en paramètre ou en valeur de retour doit être Serializable
 *   (sauf types primitifs), car il est transmis "par copie" via sérialisation.
 */
public class Token implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Valeur du token (string ou UUID par ex).
     * On pourrait utiliser un long numérique, mais String reste simple et lisible.
     */
    private final String value;

    /** Numéro de carte (identifiant usager) associé à ce token. */
    private final int userCardId;

    /** Timestamp (ms) de création du token (utile pour debug + stats). */
    private final long issuedAt;

    /** Timestamp (ms) d'expiration (permet de gérer une session limitée). */
    private final long expiresAt;

    public Token(String value, int userCardId, long issuedAt, long expiresAt) {
        this.value = Objects.requireNonNull(value, "value must not be null");
        this.userCardId = userCardId;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }

    public String getValue() {
        return value;
    }

    public int getUserCardId() {
        return userCardId;
    }

    public long getIssuedAt() {
        return issuedAt;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    /**
     * Méthode pratique pour la validation côté client/serveur.
     * (Côté serveur on fera aussi une validation plus stricte via stockage des tokens.)
     */
    public boolean isExpired(long nowMillis) {
        return nowMillis >= expiresAt;
    }

    @Override
    public String toString() {
        return "Token{value='" + value + "', userCardId=" + userCardId +
                ", issuedAt=" + issuedAt + ", expiresAt=" + expiresAt + "}";
    }
}
