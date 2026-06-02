package parttool.auth.server.model;

/**
 * Enum métier : état possible d'un utilisateur.
 * - ACTIF : compte utilisable
 * - INACTIF : compte désactivé (ex: admin, fraude, etc.)
 *
 * Pourquoi enum ?
 * - On force un ensemble fini de valeurs, évite les chaînes "ACTIF"/"INACTIF" partout.
 */
public enum UserStatus {
	ACTIF, 
	INACTIF
}
