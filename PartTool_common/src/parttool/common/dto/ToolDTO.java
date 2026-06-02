package parttool.common.dto;

import java.io.Serializable;

/**
 * DTO (Data Transfer Object) représentant un outil.
 *
 * Objectif :
 * - Transporter les données d'un outil du client vers le serveur Tools (V2),
 *   puis éventuellement du serveur vers le client.
 *
 * Remarque RMI importante :
 * - Les objets échangés à distance doivent être Serializable
 *   car RMI transmet les objets par sérialisation (passage "par copie").
 *
 * Pour V2 :
 * - On met des champs simples, suffisants pour décrire un outil.
 * - On pourra enrichir en V3/V4 si le sujet l'exige.
 */
public class ToolDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // Nom de l'outil (ex: "Perceuse", "Tournevis", "Tondeuse"...)
    private final String name;

    // Description libre (ex: "Perceuse sans fil 18V")
    private final String description;

    // Catégorie de l'outil (enum) : optionnel mais propre (évite les chaînes libres)
    private final ToolCategory category;

    // Caractéristiques physiques (exemples)
    private final double weightKg;
    private final double lengthCm;
    private final double widthCm;
    private final double heightCm;

    public ToolDTO(String name,
                   String description,
                   ToolCategory category,
                   double weightKg,
                   double lengthCm,
                   double widthCm,
                   double heightCm) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.weightKg = weightKg;
        this.lengthCm = lengthCm;
        this.widthCm = widthCm;
        this.heightCm = heightCm;
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

    @Override
    public String toString() {
        return "ToolDTO{name='" + name + "', category=" + category +
                ", weightKg=" + weightKg +
                ", LxWxH=" + lengthCm + "x" + widthCm + "x" + heightCm +
                ", description='" + description + "'}";
    }
}
