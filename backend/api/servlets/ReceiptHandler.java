package api.servlets;

import api.HttpUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import models.Receipt;
import repositories.*;
import services.ReceiptService;

import java.io.IOException;
import java.util.Map;

public class ReceiptHandler implements HttpHandler {
    private final ReceiptService receiptService;

    public ReceiptHandler() {
        this.receiptService = new ReceiptService(
            new ReceiptRepositoryImpl(),
            new BookingRepositoryImpl(),
            new UserRepositoryImpl(),
            new ShowRepositoryImpl(),
            new MovieRepositoryImpl(),
            new TheatreRepositoryImpl(),
            new PaymentRepositoryImpl()
        );
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (HttpUtils.handleCors(exchange)) return;

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();

        try {
            if (method.equals("POST")) {
                handleGenerateReceipt(exchange);
            } else if (method.equals("GET")) {
                if (query != null) {
                    Map<String, String> params = HttpUtils.parseQueryParams(query);
                    if (params.containsKey("bookingId")) {
                        handleGetReceiptByBooking(exchange, params.get("bookingId"));
                    } else {
                        HttpUtils.sendErrorResponse(exchange, 400, "bookingId parameter required");
                    }
                } else {
                    HttpUtils.sendErrorResponse(exchange, 400, "bookingId parameter required");
                }
            } else {
                HttpUtils.sendErrorResponse(exchange, 405, "Method not allowed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpUtils.sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleGenerateReceipt(HttpExchange exchange) throws IOException {
        Map<String, String> requestData = HttpUtils.readRequestBody(exchange, Map.class);
        String bookingId = requestData.get("bookingId");

        Receipt receipt = receiptService.generateReceipt(bookingId);
        HttpUtils.sendJsonResponse(exchange, 201, receipt);
    }

    private void handleGetReceiptByBooking(HttpExchange exchange, String bookingId) throws IOException {
        Receipt receipt = receiptService.getReceiptByBooking(bookingId);
        if (receipt != null) {
            HttpUtils.sendJsonResponse(exchange, 200, receipt);
        } else {
            HttpUtils.sendErrorResponse(exchange, 404, "Receipt not found");
        }
    }
}
