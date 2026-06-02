package parttool.common.dto;

import java.io.Serializable;

/**
 * Représente l'état actuel d'un outil dans le système.
 *
 * Enum = ensemble fermé de valeurs.
 * Serializable : cohérent avec les échanges RMI.
 */
public enum ToolStatus implements Serializable {

    AVAILABLE,	// disponible 
    BORROWED	// emprunté
}
