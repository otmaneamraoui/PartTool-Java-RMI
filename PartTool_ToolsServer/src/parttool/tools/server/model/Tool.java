package parttool.tools.server.model;

import parttool.common.dto.ToolCategory;
import parttool.common.dto.ToolStatus;

/**
 * Modèle métier interne au serveur Tools.
 *
 * Différence avec ToolDTO :
 * - ToolDTO : objet de transfert (client <-> serveur), Serializable, sans logique interne.
 * - Tool    : modèle interne serveur, contient aussi des infos internes (ownerCardId, id, qrId, status).
 *
 * En V2 :
 * - ownerCardId : identifie le propriétaire (issu du token / de l'utilisateur connecté).
 * - qrId : identifiant affichable (simule le QR code).
 * - status : AVAILABLE au départ (l'emprunt viendra en V4).
 */
public class Tool {

    private final long id;           // identifiant interne unique côté serveur
    private final String qrId;       // identifiant QR simulé (ex: QR-000001)
    private final int ownerCardId;   // propriétaire (numéro de carte)

    private final String name;
    private final String description;
    private final ToolCategory category;

    private final double weightKg;
    private final double lengthCm;
    private final double widthCm;
    private final double heightCm;

    // Le statut peut évoluer (V4 : emprunt/restitution)
    private ToolStatus status;

    public Tool(long id,
                String qrId,
                int ownerCardId,
                String name,
                String description,
                ToolCategory category,
                double weightKg,
                double lengthCm,
                double widthCm,
                double heightCm,
                ToolStatus status) {
        this.id = id;
        this.qrId = qrId;
        this.ownerCardId = ownerCardId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.weightKg = weightKg;
        this.lengthCm = lengthCm;
        this.widthCm = widthCm;
        this.heightCm = heightCm;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public String getQrId() {
        return qrId;
    }

    public int getOwnerCardId() {
        return ownerCardId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ToolCategory getCategory() {
        return category;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public double getLengthCm() {
        return lengthCm;
    }

    public double getWidthCm() {
        return widthCm;
    }

    public double getHeightCm() {
        return heightCm;
    }

    public ToolStatus getStatus() {
        return status;
    }

    public void setStatus(ToolStatus status) {
        this.status = status;
    }
}
