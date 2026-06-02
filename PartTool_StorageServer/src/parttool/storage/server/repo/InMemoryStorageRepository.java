package parttool.storage.server.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import parttool.storage.server.model.StorageLocation;

/**
 * Implémentation en mémoire du stockage des locaux.
 *
 * Pourquoi ConcurrentHashMap ?
 * - le serveur RMI peut recevoir plusieurs requêtes simultanément
 * - cette structure est thread-safe
 *
 * Structure :
 * - id -> StorageLocation
 */
public class InMemoryStorageRepository implements StorageRepository {

	// pour stocker les locaux en mémoire, on utilise une map id -> StorageLocation
    private final Map<Integer, StorageLocation> locations = new ConcurrentHashMap<>();

    // pour lister tous les locaux, on retourne une copie de la liste des valeurs
    @Override
    public List<StorageLocation> findAll() {
        return new ArrayList<>(locations.values());
    }

    // recherche par id
    @Override
    public StorageLocation findById(int id) {
        return locations.get(id);
    }

    // sauvegarde : on ajoute ou remplace le local dans la map
    @Override
    public StorageLocation save(StorageLocation location) {
        locations.put(location.getId(), location);
        return location;
    }
}