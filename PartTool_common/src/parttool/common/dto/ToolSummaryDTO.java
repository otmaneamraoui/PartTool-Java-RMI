package parttool.common.dto;

import java.io.Serializable;

/**
 * DTO léger pour l'affichage en liste.
 *
 * Pourquoi ?
 * - Quand on liste des objets, on n'a pas besoin de tous les détails.
 * - C'est plus efficace et plus clair côté UI.
 */
public class ToolSummaryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long toolId;
    private final String qrId;
    private final String name;
    private final ToolStatus status;

    public ToolSummaryDTO(long toolId, String qrId, String name, ToolStatus status) {
        this.toolId = toolId;
        this.qrId = qrId;
        this.name = name;
        this.status = status;
    }

    public long getToolId() {
        return toolId;
    }

    public String getQrId() {
        return qrId;
    }

    public String getName() {
        return name;
    }

    public ToolStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ToolSummaryDTO{toolId=" + toolId +
                ", qrId='" + qrId + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}
