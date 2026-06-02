package parttool.storage.server.repo;

import java.util.List;

import parttool.storage.server.model.StorageLocation;

/**
 * Interface de stockage des locaux de stockage.
 *
 * Rôle :
 * - définir les opérations de gestion des locaux
 * - séparer la logique métier du stockage des données
 *
 * Dans V3 :
 * - on utilisera une implémentation en mémoire (HashMap)
 */
public interface StorageRepository {

    /**
     * Retourne tous les locaux connus par le serveur.
     */
    List<StorageLocation> findAll();

    /**
     * Recherche un local par son identifiant.
     *
     * @param id identifiant du local
     * @return StorageLocation ou null si introuvable
     */
    StorageLocation findById(int id);

    /**
     * Sauvegarde un local.
     */
    StorageLocation save(StorageLocation location);
}