package parttool.tools.server.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import parttool.tools.server.model.Tool;

/**
 * Implémentation en mémoire du stockage des outils (V2).
 *
 * Remarque importante :
 * - RMI peut exécuter des requêtes en parallèle.
 * - On utilise donc ConcurrentHashMap pour éviter les problèmes de concurrence.
 *
 * Structure :
 * - toolsById : id -> Tool (stockage principal)
 * - idByQr    : qrId -> id  (index pour retrouver vite un outil par qrId)
 */
public class InMemoryToolRepository implements ToolRepository {

    private final Map<Long, Tool> toolsById = new ConcurrentHashMap<>();
    private final Map<String, Long> idByQr = new ConcurrentHashMap<>();

    @Override
    public Tool save(Tool tool) {
        // On stocke l'outil
        toolsById.put(tool.getId(), tool);

        // On maintient l'index qrId -> id
        if (tool.getQrId() != null) {
            idByQr.put(tool.getQrId(), tool.getId());
        }

        return tool;
    }

    @Override
    public List<Tool> findByOwner(int ownerCardId) {
        // V2 : recherche simple en parcourant la map
        // (suffisant pour un TP / petit volume).
        List<Tool> result = new ArrayList<>();
        for (Tool t : toolsById.values()) {
            if (t.getOwnerCardId() == ownerCardId) {
                result.add(t);
            }
        }
        return result;
    }

    @Override
    public Tool findByQrId(String qrId) {
        if (qrId == null) return null;

        Long id = idByQr.get(qrId);
        if (id == null) return null;

        return toolsById.get(id);
    }
}
