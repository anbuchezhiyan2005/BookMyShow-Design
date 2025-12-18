package api.servlets;

import api.HttpUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import models.Seat;
import repositories.SeatRepositoryImpl;
import services.SeatService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SeatHandler implements HttpHandler {
    private final SeatService seatService;

    public SeatHandler() {
        this.seatService = new SeatService(new SeatRepositoryImpl());
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (HttpUtils.handleCors(exchange)) return;

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();

        try {
            if (method.equals("GET")) {
                if (query != null) {
                    Map<String, String> params = HttpUtils.parseQueryParams(query);
                    if (params.containsKey("showId")) {
                        String available = params.get("available");
                        if ("true".equals(available)) {
                            handleGetAvailableSeats(exchange, params.get("showId"));
                        } else {
                            handleGetSeatsByShow(exchange, params.get("showId"));
                        }
                    } else {
                        HttpUtils.sendErrorResponse(exchange, 400, "showId parameter required");
                    }
                } else {
                    HttpUtils.sendErrorResponse(exchange, 400, "showId parameter required");
                }
            } else if (method.equals("POST")) {
                if (path.endsWith("/block")) {
                    handleBlockSeats(exchange);
                } else if (path.endsWith("/release")) {
                    handleReleaseSeats(exchange);
                } else {
                    handleAddSeat(exchange);
                }
            } else {
                HttpUtils.sendErrorResponse(exchange, 405, "Method not allowed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpUtils.sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleGetSeatsByShow(HttpExchange exchange, String showId) throws IOException {
        List<Seat> seats = seatService.getSeatsByShow(showId);
        HttpUtils.sendJsonResponse(exchange, 200, seats);
    }

    private void handleGetAvailableSeats(HttpExchange exchange, String showId) throws IOException {
        List<Seat> seats = seatService.getAvailableSeats(showId);
        HttpUtils.sendJsonResponse(exchange, 200, seats);
    }

    private void handleAddSeat(HttpExchange exchange) throws IOException {
        Seat seat = HttpUtils.readRequestBody(exchange, Seat.class);
        seat.setSeatId("SEAT_" + System.currentTimeMillis());
        Seat saved = seatService.addSeat(seat);
        HttpUtils.sendJsonResponse(exchange, 201, saved);
    }

    private void handleBlockSeats(HttpExchange exchange) throws IOException {
        Map<String, Object> requestData = HttpUtils.readRequestBody(exchange, Map.class);
        List<String> seatIds = (List<String>) requestData.get("seatIds");
        
        boolean blocked = seatService.blockSeats(seatIds);
        Map<String, Object> response = Map.of("success", blocked, "message", blocked ? "Seats blocked successfully" : "Failed to block seats");
        HttpUtils.sendJsonResponse(exchange, 200, response);
    }

    private void handleReleaseSeats(HttpExchange exchange) throws IOException {
        Map<String, Object> requestData = HttpUtils.readRequestBody(exchange, Map.class);
        List<String> seatIds = (List<String>) requestData.get("seatIds");
        
        boolean released = seatService.releaseSeats(seatIds);
        Map<String, Object> response = Map.of("success", released, "message", released ? "Seats released successfully" : "Failed to release seats");
        HttpUtils.sendJsonResponse(exchange, 200, response);
    }
}
