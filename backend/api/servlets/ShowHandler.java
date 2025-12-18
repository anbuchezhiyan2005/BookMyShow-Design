package api.servlets;

import api.HttpUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import models.Show;
import repositories.ShowRepositoryImpl;
import repositories.cached.CachedShowRepository;
import services.ShowService;
import cache.InMemoryCache;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ShowHandler implements HttpHandler {
    private final ShowService showService;

    public ShowHandler() {
        InMemoryCache cache = new InMemoryCache();
        this.showService = new ShowService(
            new CachedShowRepository(new ShowRepositoryImpl(), cache)
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
                String showId = HttpUtils.extractPathParam(path, "/api/shows");
                if (showId != null) {
                    handleGetShow(exchange, showId);
                } else if (query != null) {
                    Map<String, String> params = HttpUtils.parseQueryParams(query);
                    if (params.containsKey("movieId") && params.containsKey("theatreId")) {
                        handleGetByMovieAndTheatre(exchange, params.get("movieId"), params.get("theatreId"));
                    } else if (params.containsKey("movieId")) {
                        handleGetByMovie(exchange, params.get("movieId"));
                    } else if (params.containsKey("theatreId")) {
                        handleGetByTheatre(exchange, params.get("theatreId"));
                    } else {
                        handleGetAllShows(exchange);
                    }
                } else {
                    handleGetAllShows(exchange);
                }
            } else if (method.equals("POST")) {
                handleAddShow(exchange);
            } else {
                HttpUtils.sendErrorResponse(exchange, 405, "Method not allowed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpUtils.sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleGetAllShows(HttpExchange exchange) throws IOException {
        // Return empty list for now - frontend should use filters
        HttpUtils.sendJsonResponse(exchange, 200, List.of());
    }

    private void handleGetShow(HttpExchange exchange, String showId) throws IOException {
        Show show = showService.getShowById(showId);
        if (show != null) {
            HttpUtils.sendJsonResponse(exchange, 200, show);
        } else {
            HttpUtils.sendErrorResponse(exchange, 404, "Show not found");
        }
    }

    private void handleGetByMovie(HttpExchange exchange, String movieId) throws IOException {
        List<Show> shows = showService.getShowsByMovie(movieId);
        HttpUtils.sendJsonResponse(exchange, 200, shows);
    }

    private void handleGetByTheatre(HttpExchange exchange, String theatreId) throws IOException {
        List<Show> shows = showService.getShowsByTheatre(theatreId);
        HttpUtils.sendJsonResponse(exchange, 200, shows);
    }

    private void handleGetByMovieAndTheatre(HttpExchange exchange, String movieId, String theatreId) throws IOException {
        List<Show> shows = showService.getShowsByMovie(movieId).stream()
            .filter(s -> s.getTheatreId().equals(theatreId))
            .toList();
        HttpUtils.sendJsonResponse(exchange, 200, shows);
    }

    private void handleAddShow(HttpExchange exchange) throws IOException {
        Show show = HttpUtils.readRequestBody(exchange, Show.class);
        show.setShowId("SHOW_" + System.currentTimeMillis());
        Show saved = showService.addShow(show);
        HttpUtils.sendJsonResponse(exchange, 201, saved);
    }
}
