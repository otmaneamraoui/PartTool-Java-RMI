package parttool.tools.server.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Générateur d'identifiants pour le serveur Tools (V2).
 *
 * Objectif :
 * - Créer un identifiant interne "toolId" (long) : unique côté serveur.
 * - Créer un identifiant "qrId" (String) : à afficher / simuler un QR code.
 *
 * Pourquoi AtomicLong ?
 * - RMI peut traiter plusieurs requêtes en parallèle (multi-threads).
 * - AtomicLong garantit que les IDs restent uniques même avec plusieurs appels simultanés.
 *
 * Exemple de qrId :
 * - QR-000001, QR-000002, ...
 */
public class QrIdGenerator {

    private final AtomicLong toolIdCounter = new AtomicLong(0);

    /**
     * Génère un nouvel identifiant interne unique.
     */
    public long nextToolId() {
        return toolIdCounter.incrementAndGet();
    }

    /**
     * Génère un identifiant "QR" lisible.
     * On l'aligne sur le toolId pour rester simple en V2.
     */
    public String nextQrId(long toolId) {
        // Formatage sur 6 chiffres : QR-000001, QR-000002...
        return String.format("QR-%06d", toolId);
    }
}
