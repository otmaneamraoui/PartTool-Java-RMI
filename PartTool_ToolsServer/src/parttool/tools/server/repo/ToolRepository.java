package parttool.tools.server.repo;

import java.util.List;

import parttool.tools.server.model.Tool;

/**
 * Repository = couche d'accès aux données (ici aux outils).
 *
 * Pourquoi une interface ?
 * - Séparer la logique métier (ToolServiceImpl) du stockage (HashMap, fichier, DB...).
 * - Rendre le code plus propre et plus facile à faire évoluer.
 *
 * V2 : stockage en mémoire.
 * V3/V4 : on pourra conserver cette interface et changer l'implémentation si besoin.
 */
public interface ToolRepository {

    /**
     * Sauvegarde un outil et retourne l'outil sauvegardé.
     */
    Tool save(Tool tool);

    /**
     * Retourne tous les outils déclarés par un propriétaire (ownerCardId).
     */
    List<Tool> findByOwner(int ownerCardId);

    /**
     * Recherche un outil par son identifiant QR (simulé).
     * Retourne null si introuvable.
     */
    Tool findByQrId(String qrId);
}
