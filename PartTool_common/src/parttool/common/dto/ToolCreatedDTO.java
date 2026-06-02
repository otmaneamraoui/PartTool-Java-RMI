package parttool.common.dto;

import java.io.Serializable;

/**
 * DTO retourné par le serveur Tools après la déclaration d'un outil.
 *
 * Il contient :
 * - toolId : identifiant interne généré côté serveur
 * - qrId   : identifiant simulant le QR code à coller sur l'outil
 * - message: message informatif (succès, etc.)
 *
 * RMI :
 * - Serializable obligatoire car cet objet est renvoyé au client.
 */
public class ToolCreatedDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long toolId;
    private final String qrId;
    private final String message;

    public ToolCreatedDTO(long toolId, String qrId, String message) {
        this.toolId = toolId;
        this.qrId = qrId;
        this.message = message;
    }

    public long getToolId() {
        return toolId;
    }

    public String getQrId() {
        return qrId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ToolCreatedDTO{toolId=" + toolId +
                ", qrId='" + qrId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
