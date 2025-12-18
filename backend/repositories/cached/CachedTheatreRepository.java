package repositories.cached;

import models.Theatre;
import repositories.TheatreRepository;
import cache.InMemoryCache;
import cache.CacheConfig;

import java.util.List;

public class CachedTheatreRepository implements TheatreRepository {
    private TheatreRepository delegate;
    private InMemoryCache cache;

    public CachedTheatreRepository(TheatreRepository delegate, InMemoryCache cache) {
        this.delegate = delegate;
        this.cache = cache;
    }

    @Override
    public Theatre save(Theatre theatre) {
        Theatre saved = delegate.save(theatre);
        // Invalidate related caches
        cache.evict(CacheConfig.THEATRE_PREFIX + theatre.getTheatreId());
        cache.evict(CacheConfig.THEATRE_ALL_KEY);
        cache.evict(CacheConfig.THEATRE_CITY_PREFIX + theatre.getCity());
        return saved;
    }

    @Override
    public Theatre findById(String theatreId) {
        String cacheKey = CacheConfig.THEATRE_PREFIX + theatreId;
        Theatre cached = cache.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        
        Theatre theatre = delegate.findById(theatreId);
        if (theatre != null) {
            cache.put(cacheKey, theatre, CacheConfig.THEATRE_TTL);
        }
        return theatre;
    }

    @Override
    public List<Theatre> findAll() {
        String cacheKey = CacheConfig.THEATRE_ALL_KEY;
        List<Theatre> cached = cache.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        
        List<Theatre> theatres = delegate.findAll();
        cache.put(cacheKey, theatres, CacheConfig.THEATRE_TTL);
        return theatres;
    }

    @Override
    public List<Theatre> findByCity(String city) {
        String cacheKey = CacheConfig.THEATRE_CITY_PREFIX + city;
        List<Theatre> cached = cache.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        
        List<Theatre> theatres = delegate.findByCity(city);
        cache.put(cacheKey, theatres, CacheConfig.THEATRE_TTL);
        return theatres;
    }

    @Override
    public boolean delete(String theatreId) {
        boolean deleted = delegate.delete(theatreId);
        if (deleted) {
            cache.evict(CacheConfig.THEATRE_PREFIX + theatreId);
            cache.evict(CacheConfig.THEATRE_ALL_KEY);
        }
        return deleted;
    }
}
