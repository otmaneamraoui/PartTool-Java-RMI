package parttool.common.dto;

import java.io.Serializable;

/**
 * Enum = liste fermée de catégories possibles.
 *
 * Pourquoi un enum ?
 * - évite les fautes de frappe ("garden", "GARDEN", "Jardin"...)
 * - le code reste cohérent partout (client + serveur)
 *
 * Serializable : un enum est déjà sérialisable, mais le préciser ne gêne pas et reste cohérent.
 */
public enum ToolCategory implements Serializable {
    BRICOLAGE,
    JARDINAGE,
    AUTOMOBILE,
    AUTRE   // autre / non classé
}
