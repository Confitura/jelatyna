package pl.confitura.jelatyna.sponsors.repository.mem;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRepository<E, K> {
    protected final Map<K, E> repo = new ConcurrentHashMap<>();
}
