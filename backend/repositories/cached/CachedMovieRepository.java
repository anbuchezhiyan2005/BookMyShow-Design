package repositories.cached;

import models.Movie;
import repositories.MovieRepository;
import cache.InMemoryCache;
import cache.CacheConfig;

import java.util.List;

public class CachedMovieRepository implements MovieRepository {
    private MovieRepository delegate;
    private InMemoryCache cache;

    public CachedMovieRepository(MovieRepository delegate, InMemoryCache cache) {
        this.delegate = delegate;
        this.cache = cache;
    }

    @Override
    public Movie save(Movie movie) {
        Movie saved = delegate.save(movie);
        // Invalidate related caches
        cache.evict(CacheConfig.MOVIE_PREFIX + movie.getMovieId());
        cache.evict(CacheConfig.MOVIE_ALL_KEY);
        cache.evict(CacheConfig.MOVIE_GENRE_PREFIX + movie.getGenre());
        return saved;
    }

    @Override
    public Movie findById(String movieId) {
        String cacheKey = CacheConfig.MOVIE_PREFIX + movieId;
        Movie cached = cache.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        
        Movie movie = delegate.findById(movieId);
        if (movie != null) {
            cache.put(cacheKey, movie, CacheConfig.MOVIE_TTL);
        }
        return movie;
    }

    @Override
    public List<Movie> findAll() {
        String cacheKey = CacheConfig.MOVIE_ALL_KEY;
        List<Movie> cached = cache.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        
        List<Movie> movies = delegate.findAll();
        cache.put(cacheKey, movies, CacheConfig.MOVIE_TTL);
        return movies;
    }

    @Override
    public List<Movie> findByTitle(String title) {
        // Don't cache search results (they vary too much)
        return delegate.findByTitle(title);
    }

    @Override
    public List<Movie> findByGenre(String genre) {
        String cacheKey = CacheConfig.MOVIE_GENRE_PREFIX + genre;
        List<Movie> cached = cache.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        
        List<Movie> movies = delegate.findByGenre(genre);
        cache.put(cacheKey, movies, CacheConfig.MOVIE_TTL);
        return movies;
    }

    @Override
    public boolean delete(String movieId) {
        boolean deleted = delegate.delete(movieId);
        if (deleted) {
            cache.evict(CacheConfig.MOVIE_PREFIX + movieId);
            cache.evict(CacheConfig.MOVIE_ALL_KEY);
        }
        return deleted;
    }
}
