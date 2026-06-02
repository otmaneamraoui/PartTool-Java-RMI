package parttool.common.dto;

import java.io.Serializable;

/**
 * DTO = Data Transfer Object.
 *
 * Pourquoi un DTO ?
 * - Ne pas exposer la classe "User" interne du serveur au client.
 * - Le client n'a besoin que de quelques infos (ex: nom, email) éventuellement.
 *
 */
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int cardId;
    private final String fullName;
    private final String email;

    public UserDTO(int cardId, String fullName, String email) {
        this.cardId = cardId;
        this.fullName = fullName;
        this.email = email;
    }

    public int getCardId() {
        return cardId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "UserDTO{cardId=" + cardId + ", fullName='" + fullName + "', email='" + email + "'}";
    }
}
