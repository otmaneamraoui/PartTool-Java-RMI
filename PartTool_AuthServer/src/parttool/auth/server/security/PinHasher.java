package parttool.auth.server.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilitaire pour "hasher" le PIN.
 *
 * Pourquoi ?
 * - Bon réflexe sécurité : on ne stocke pas le code secret en clair.
 *
 */
public final class PinHasher {
	// Classe utilitaire : on empêche l'instanciation.
    private PinHasher() { }
    // Implémentation simple : on utilise SHA-256 (pas de sel, pas de rounds, etc. pour simplifier).
    public static String sha256(String pin) {
        try {
        	// MessageDigest est une classe standard Java pour faire du hashage
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // On convertit le PIN en bytes (UTF-8) et on calcule le hash
            byte[] digest = md.digest(pin.getBytes(StandardCharsets.UTF_8));
            // On convertit le résultat en hexadécimal pour le stocker plus facilement
            return toHex(digest);
        } catch (NoSuchAlgorithmException e) {
            // Sur une JVM standard, SHA-256 existe toujours.
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    // Méthode utilitaire pour convertir un tableau de bytes en chaîne hexadécimale
    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(Character.forDigit((b >> 4) & 0xF, 16));
            sb.append(Character.forDigit(b & 0xF, 16));
        }
        return sb.toString();
    }
}
