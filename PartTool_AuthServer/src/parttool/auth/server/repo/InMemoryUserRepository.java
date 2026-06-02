package parttool.auth.server.repo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import parttool.auth.server.model.User;

// Implantation simple du UserRepository qui stocke les utilisateurs en mémoire (HashMap).
public class InMemoryUserRepository implements UserRepository {

	/* c'est quoi une ConcurrentHashMap, MAP, thread-safe, etc. ?
	 * - Map : interface de collection clé-valeur (clé unique, valeur associée)
	 * - HashMap : implémentation de Map basée sur une table de hachage (non thread-safe)
	 * - ConcurrentHashMap : implémentation de Map thread-safe, permet accès concurrent sans blocage global (utilise segmentations internes)
	 * users : Map qui associe un cardId (Integer) à un User. Permet de stocker et retrouver les utilisateurs par leur cardId.
	 * 
	 * */
    private final Map<Integer, User> users = new ConcurrentHashMap<>();

    @Override
    public boolean exists(int cardId) {
        return users.containsKey(cardId);
    }

    @Override
    public User findByCardId(int cardId) {
        return users.get(cardId);
    }

    @Override
    public void save(User user) {
        users.put(user.getCardId(), user);
    }
}
