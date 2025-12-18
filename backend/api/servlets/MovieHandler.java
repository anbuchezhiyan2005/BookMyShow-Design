package api.servlets;

import api.HttpUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import models.Movie;
import repositories.MovieRepositoryImpl;
import repositories.cached.CachedMovieRepository;
import services.MovieService;
import cache.InMemoryCache;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MovieHandler implements HttpHandler {
    private final MovieService movieService;

    public MovieHandler() {
        InMemoryCache cache = new InMemoryCache();
        this.movieService = new MovieService(
            new CachedMovieRepository(new MovieRepositoryImpl(), cache)
        );
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (HttpUtils.handleCors(exchange)) return;

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();

        try {
            if (method.equals("GET")) {
                String movieId = HttpUtils.extractPathParam(path, "/api/movies");
                if (movieId != null) {
                    handleGetMovie(exchange, movieId);
                } else if (query != null) {
                    Map<String, String> params = HttpUtils.parseQueryParams(query);
                    if (params.containsKey("title")) {
                        handleSearchByTitle(exchange, params.get("title"));
                    } else if (params.containsKey("genre")) {
                        handleGetByGenre(exchange, params.get("genre"));
                    } else {
                        handleGetAllMovies(exchange);
                    }
                } else {
                    handleGetAllMovies(exchange);
                }
            } else if (method.equals("POST")) {
                handleAddMovie(exchange);
            } else {
                HttpUtils.sendErrorResponse(exchange, 405, "Method not allowed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpUtils.sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleGetAllMovies(HttpExchange exchange) throws IOException {
        List<Movie> movies = movieService.getAllMovies();
        HttpUtils.sendJsonResponse(exchange, 200, movies);
    }

    private void handleGetMovie(HttpExchange exchange, String movieId) throws IOException {
        Movie movie = movieService.getMovieById(movieId);
        if (movie != null) {
            HttpUtils.sendJsonResponse(exchange, 200, movie);
        } else {
            HttpUtils.sendErrorResponse(exchange, 404, "Movie not found");
        }
    }

    private void handleSearchByTitle(HttpExchange exchange, String title) throws IOException {
        List<Movie> movies = movieService.searchMoviesByTitle(title);
        HttpUtils.sendJsonResponse(exchange, 200, movies);
    }

    private void handleGetByGenre(HttpExchange exchange, String genre) throws IOException {
        List<Movie> movies = movieService.getMoviesByGenre(genre);
        HttpUtils.sendJsonResponse(exchange, 200, movies);
    }

    private void handleAddMovie(HttpExchange exchange) throws IOException {
        Movie movie = HttpUtils.readRequestBody(exchange, Movie.class);
        movie.setMovieId("MOVIE_" + System.currentTimeMillis());
        Movie saved = movieService.addMovie(movie);
        HttpUtils.sendJsonResponse(exchange, 201, saved);
    }
}
