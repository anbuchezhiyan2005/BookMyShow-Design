package cache;

public class CacheEntry<T> {
    private T value;
    private long expiryTime;

    public CacheEntry(T value, long ttlMillis) {
        this.value = value;
        this.expiryTime = System.currentTimeMillis() + ttlMillis;
    }

    public T getValue() {
        return value;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }

    public long getExpiryTime() {
        return expiryTime;
    }
}
