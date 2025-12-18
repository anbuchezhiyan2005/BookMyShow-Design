package cache;

public class CacheConfig {
    // TTL in milliseconds
    public static final long MOVIE_TTL = 60 * 60 * 1000;      // 1 hour
    public static final long THEATRE_TTL = 30 * 60 * 1000;    // 30 minutes
    public static final long SHOW_TTL = 2 * 60 * 1000;        // 2 minutes

    // Cache key prefixes
    public static final String MOVIE_PREFIX = "movie:";
    public static final String MOVIE_ALL_KEY = "movie:all";
    public static final String MOVIE_GENRE_PREFIX = "movie:genre:";
    
    public static final String THEATRE_PREFIX = "theatre:";
    public static final String THEATRE_ALL_KEY = "theatre:all";
    public static final String THEATRE_CITY_PREFIX = "theatre:city:";
    
    public static final String SHOW_PREFIX = "show:";
    public static final String SHOW_MOVIE_PREFIX = "show:movie:";
    public static final String SHOW_THEATRE_PREFIX = "show:theatre:";
}
