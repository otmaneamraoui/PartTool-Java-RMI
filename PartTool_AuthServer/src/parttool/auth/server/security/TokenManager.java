package parttool.auth.server.security;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import parttool.common.dto.Token;

/**
 * Gestion simple des tokens pour :
 * - Génère un token aléatoire
 * - Stocke les tokens actifs
 * - Permet validate/logout
 *
 * Pourquoi c'est utile ?
 * - Jeton réutilisable et vérifiable par d'autres serveurs.
 * - Ici, validate() permet cette vérification.
 */
public class TokenManager {

    // Stockage tokenValue -> Token (token actif)
    private final Map<String, Token> tokenStore = new ConcurrentHashMap<>();

    private final SecureRandom random = new SecureRandom();

    // Durée de vie session (ex: 30 min) - modifiable
    private final long ttlMillis;

    public TokenManager(long ttlMillis) {
        this.ttlMillis = ttlMillis;
    }

    // Génère un token pour une carte donnée (après authentification réussie)
    public Token createToken(int cardId) {
        long now = System.currentTimeMillis(); // timestamp actuel en ms
        long exp = now + ttlMillis; // timestamp d'expiration

        // tokenValue = random bytes encodés Base64 URL-safe
        byte[] buf = new byte[24];
        random.nextBytes(buf);
        // Encodage URL-safe sans padding
        String value = Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
        // token = valeur + info (cardId, timestamps)
        Token token = new Token(value, cardId, now, exp);
        tokenStore.put(value, token); // Stockage du token actif 
        return token;
    }

    // Valide un token : existe-t-il ? et n'est-il pas expiré ?
    public boolean validate(Token token) {
        if (token == null) return false;

        Token stored = tokenStore.get(token.getValue());
        if (stored == null) return false;

        long now = System.currentTimeMillis();
        if (stored.isExpired(now)) {
            // Token expiré => on le retire pour nettoyer
            tokenStore.remove(stored.getValue());
            return false;
        }
        return true;
    }
    
    // Invalide un token (ex: lors du logout)
    public void invalidate(Token token) {
        if (token != null) {
            tokenStore.remove(token.getValue());
        }
    }
}
