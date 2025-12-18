package repositories.cached;

import models.Show;
import repositories.ShowRepository;
import cache.InMemoryCache;
import cache.CacheConfig;

import java.util.List;

public class CachedShowRepository implements ShowRepository {
    private ShowRepository delegate;
    private InMemoryCache cache;

    public CachedShowRepository(ShowRepository delegate, InMemoryCache cache) {
        this.delegate = delegate;
        this.cache = cache;
    }

    @Override
    public Show save(Show show) {
        Show saved = delegate.save(show);
        // Invalidate related caches
        cache.evict(CacheConfig.SHOW_PREFIX + show.getShowId());
        cache.evict(CacheConfig.SHOW_MOVIE_PREFIX + show.getMovieId());
        cache.evict(CacheConfig.SHOW_THEATRE_PREFIX + show.getTheatreId());
        return saved;
    }

    @Override
    public Show findById(String showId) {
        String cacheKey = CacheConfig.SHOW_PREFIX + showId;
        Show cached = cache.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        
        Show show = delegate.findById(showId);
        if (show != null) {
            cache.put(cacheKey, show, CacheConfig.SHOW_TTL);
        }
        return show;
    }

    @Override
    public List<Show> findByMovieId(String movieId) {
        String cacheKey = CacheConfig.SHOW_MOVIE_PREFIX + movieId;
        List<Show> cached = cache.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        
        List<Show> shows = delegate.findByMovieId(movieId);
        cache.put(cacheKey, shows, CacheConfig.SHOW_TTL);
        return shows;
    }

    @Override
    public List<Show> findByTheatreId(String theatreId) {
        String cacheKey = CacheConfig.SHOW_THEATRE_PREFIX + theatreId;
        List<Show> cached = cache.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        
        List<Show> shows = delegate.findByTheatreId(theatreId);
        cache.put(cacheKey, shows, CacheConfig.SHOW_TTL);
        return shows;
    }

    @Override
    public boolean updateAvailableSeats(String showId, int availableSeats) {
        boolean updated = delegate.updateAvailableSeats(showId, availableSeats);
        if (updated) {
            // Evict show cache when seats are updated
            cache.evict(CacheConfig.SHOW_PREFIX + showId);
        }
        return updated;
    }

    @Override
    public boolean delete(String showId) {
        boolean deleted = delegate.delete(showId);
        if (deleted) {
            cache.evict(CacheConfig.SHOW_PREFIX + showId);
        }
        return deleted;
    }
}
