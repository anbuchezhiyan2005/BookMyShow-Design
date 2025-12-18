package api.servlets;

import api.HttpUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import models.Booking;
import repositories.*;
import services.BookingService;
import services.SeatService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BookingHandler implements HttpHandler {
    private final BookingService bookingService;

    public BookingHandler() {
        this.bookingService = new BookingService(
            new BookingRepositoryImpl(),
            new ShowRepositoryImpl(),
            new SeatService(new SeatRepositoryImpl())
        );
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (HttpUtils.handleCors(exchange)) return;

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();

        try {
            if (method.equals("POST") && path.endsWith("/confirm")) {
                // POST /api/bookings/{bookingId}/confirm
                String bookingId = path.split("/")[3];
                handleConfirmBooking(exchange, bookingId);
            } else if (method.equals("POST") && path.endsWith("/cancel")) {
                // POST /api/bookings/{bookingId}/cancel
                String bookingId = path.split("/")[3];
                handleCancelBooking(exchange, bookingId);
            } else if (method.equals("POST")) {
                // POST /api/bookings
                handleCreateBooking(exchange);
            } else if (method.equals("GET")) {
                String bookingId = HttpUtils.extractPathParam(path, "/api/bookings");
                if (bookingId != null) {
                    handleGetBooking(exchange, bookingId);
                } else if (query != null) {
                    Map<String, String> params = HttpUtils.parseQueryParams(query);
                    if (params.containsKey("userId")) {
                        handleGetUserBookings(exchange, params.get("userId"));
                    } else {
                        HttpUtils.sendErrorResponse(exchange, 400, "userId parameter required");
                    }
                } else {
                    HttpUtils.sendErrorResponse(exchange, 400, "userId parameter or bookingId required");
                }
            } else {
                HttpUtils.sendErrorResponse(exchange, 405, "Method not allowed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpUtils.sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleCreateBooking(HttpExchange exchange) throws IOException {
        Map<String, Object> requestData = HttpUtils.readRequestBody(exchange, Map.class);
        String userId = (String) requestData.get("userId");
        String showId = (String) requestData.get("showId");
        List<String> seatIds = (List<String>) requestData.get("seatIds");

        Booking booking = bookingService.createBooking(userId, showId, seatIds);
        HttpUtils.sendJsonResponse(exchange, 201, booking);
    }

    private void handleConfirmBooking(HttpExchange exchange, String bookingId) throws IOException {
        Booking booking = bookingService.confirmBooking(bookingId);
        HttpUtils.sendJsonResponse(exchange, 200, booking);
    }

    private void handleCancelBooking(HttpExchange exchange, String bookingId) throws IOException {
        boolean cancelled = bookingService.cancelBooking(bookingId);
        Map<String, Object> response = Map.of("cancelled", cancelled);
        HttpUtils.sendJsonResponse(exchange, 200, response);
    }

    private void handleGetBooking(HttpExchange exchange, String bookingId) throws IOException {
        Booking booking = bookingService.getBookingById(bookingId);
        if (booking != null) {
            HttpUtils.sendJsonResponse(exchange, 200, booking);
        } else {
            HttpUtils.sendErrorResponse(exchange, 404, "Booking not found");
        }
    }

    private void handleGetUserBookings(HttpExchange exchange, String userId) throws IOException {
        List<Booking> bookings = bookingService.getUserBookings(userId);
        HttpUtils.sendJsonResponse(exchange, 200, bookings);
    }
}
