package parttool.auth.server.model;

import java.util.Objects;

/**
 * Modèle interne côté serveur.
 *
 * - Elle stocke des infos internes (ex: pinHash) qu'on ne veut pas exposer.
 */
public class User {

    private final int cardId;
    private final String pinHash; // on ne stocke JAMAIS le PIN en clair
    private final String fullName;
    private final String email;
    private UserStatus status;

    // Constructeur
    public User(int cardId, String pinHash, String fullName, String email, UserStatus status) {
        this.cardId = cardId;
        this.pinHash = Objects.requireNonNull(pinHash, "pinHash must not be null");
        this.fullName = fullName;
        this.email = email;
        this.status = status == null ? UserStatus.ACTIF : status;
    }

    public int getCardId() {
        return cardId;
    }

    public String getPinHash() {
        return pinHash;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
