package cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryCache {
    private Map<String, CacheEntry<?>> cache;
    private AtomicLong hits;
    private AtomicLong misses;

    public InMemoryCache() {
        this.cache = new ConcurrentHashMap<>();
        this.hits = new AtomicLong(0);
        this.misses = new AtomicLong(0);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        CacheEntry<?> entry = cache.get(key);
        if (entry != null && !entry.isExpired()) {
            hits.incrementAndGet();
            return (T) entry.getValue();
        }
        if (entry != null) {
            cache.remove(key); // Remove expired entry
        }
        misses.incrementAndGet();
        return null;
    }

    public <T> void put(String key, T value, long ttlMillis) {
        cache.put(key, new CacheEntry<>(value, ttlMillis));
    }

    public void evict(String key) {
        cache.remove(key);
    }

    public void evictAll() {
        cache.clear();
    }

    public long getHits() {
        return hits.get();
    }

    public long getMisses() {
        return misses.get();
    }

    public long getTotalRequests() {
        return hits.get() + misses.get();
    }

    public double getHitRate() {
        long total = getTotalRequests();
        return total == 0 ? 0.0 : (hits.get() * 100.0) / total;
    }

    public int getSize() {
        return cache.size();
    }

    public void printStats() {
        System.out.println("\n========================================");
        System.out.println("         CACHE STATISTICS");
        System.out.println("========================================");
        System.out.println("Total Requests: " + getTotalRequests());
        System.out.println("Cache Hits: " + getHits());
        System.out.println("Cache Misses: " + getMisses());
        System.out.println("Hit Rate: " + String.format("%.2f%%", getHitRate()));
        System.out.println("Cache Size: " + getSize() + " entries");
        System.out.println("========================================\n");
    }
}
