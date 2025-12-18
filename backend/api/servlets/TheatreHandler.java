package api.servlets;

import api.HttpUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import models.Theatre;
import repositories.TheatreRepositoryImpl;
import repositories.ShowRepositoryImpl;
import repositories.cached.CachedTheatreRepository;
import services.TheatreService;
import cache.InMemoryCache;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TheatreHandler implements HttpHandler {
    private final TheatreService theatreService;

    public TheatreHandler() {
        InMemoryCache cache = new InMemoryCache();
        this.theatreService = new TheatreService(
            new CachedTheatreRepository(new TheatreRepositoryImpl(), cache),
            new ShowRepositoryImpl()
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
                String theatreId = HttpUtils.extractPathParam(path, "/api/theatres");
                if (theatreId != null) {
                    handleGetTheatre(exchange, theatreId);
                } else if (query != null) {
                    Map<String, String> params = HttpUtils.parseQueryParams(query);
                    if (params.containsKey("movieId")) {
                        handleGetByMovie(exchange, params.get("movieId"));
                    } else if (params.containsKey("city")) {
                        handleGetByCity(exchange, params.get("city"));
                    } else {
                        handleGetAllTheatres(exchange);
                    }
                } else {
                    handleGetAllTheatres(exchange);
                }
            } else if (method.equals("POST")) {
                handleAddTheatre(exchange);
            } else {
                HttpUtils.sendErrorResponse(exchange, 405, "Method not allowed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpUtils.sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleGetAllTheatres(HttpExchange exchange) throws IOException {
        List<Theatre> theatres = theatreService.getAllTheatres();
        HttpUtils.sendJsonResponse(exchange, 200, theatres);
    }

    private void handleGetTheatre(HttpExchange exchange, String theatreId) throws IOException {
        Theatre theatre = theatreService.getTheatreById(theatreId);
        if (theatre != null) {
            HttpUtils.sendJsonResponse(exchange, 200, theatre);
        } else {
            HttpUtils.sendErrorResponse(exchange, 404, "Theatre not found");
        }
    }

    private void handleGetByMovie(HttpExchange exchange, String movieId) throws IOException {
        List<Theatre> theatres = theatreService.getTheatresByMovie(movieId);
        HttpUtils.sendJsonResponse(exchange, 200, theatres);
    }

    private void handleGetByCity(HttpExchange exchange, String city) throws IOException {
        List<Theatre> theatres = theatreService.getTheatresByCity(city);
        HttpUtils.sendJsonResponse(exchange, 200, theatres);
    }

    private void handleAddTheatre(HttpExchange exchange) throws IOException {
        Theatre theatre = HttpUtils.readRequestBody(exchange, Theatre.class);
        theatre.setTheatreId("THEATRE_" + System.currentTimeMillis());
        Theatre saved = theatreService.addTheatre(theatre);
        HttpUtils.sendJsonResponse(exchange, 201, saved);
    }
}
