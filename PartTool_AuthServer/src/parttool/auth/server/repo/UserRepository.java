package parttool.auth.server.repo;

import parttool.auth.server.model.User;

/**
 * Repository = couche d'accès aux données utilisateurs.
 *
 * Pourquoi c'est utile ?
 * - Séparer le "métier" (AuthServiceImpl) du "stockage" (HashMap).
 * - Plus tard (si besoin), on pourrait remplacer InMemory par une base de données.
 */
// contrat d'accès aux données utilisateurs (sans exposer les détails de stockage).
public interface UserRepository {
	// permet de vérifier si une carte est déjà enregistrée (ex: lors de l'inscription).
    boolean exists(int cardId);
    // récupère un utilisateur par son cardId (ex: lors de l'authentification).
    User findByCardId(int cardId);
    // enregistre un nouvel utilisateur (ex: lors de l'inscription).
    void save(User user);
}
