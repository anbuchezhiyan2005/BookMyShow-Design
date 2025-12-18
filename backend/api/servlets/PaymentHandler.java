package api.servlets;

import api.HttpUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import models.Payment;
import repositories.PaymentRepositoryImpl;
import repositories.BookingRepositoryImpl;
import services.PaymentService;
import strategy.*;

import java.io.IOException;
import java.util.Map;

public class PaymentHandler implements HttpHandler {
    private final PaymentService paymentService;

    public PaymentHandler() {
        this.paymentService = new PaymentService(
            new PaymentRepositoryImpl(),
            new BookingRepositoryImpl()
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
                handleProcessPayment(exchange);
            } else if (method.equals("GET")) {
                if (query != null) {
                    Map<String, String> params = HttpUtils.parseQueryParams(query);
                    if (params.containsKey("bookingId")) {
                        handleGetPaymentByBooking(exchange, params.get("bookingId"));
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

    private void handleProcessPayment(HttpExchange exchange) throws IOException {
        Map<String, Object> requestData = HttpUtils.readRequestBody(exchange, Map.class);
        String bookingId = (String) requestData.get("bookingId");
        double amount = ((Number) requestData.get("amount")).doubleValue();
        String paymentMethod = (String) requestData.get("paymentMethod");
        Map<String, String> paymentDetails = (Map<String, String>) requestData.get("paymentDetails");

        // Create payment strategy based on method
        PaymentStrategy strategy = createPaymentStrategy(paymentMethod, paymentDetails);

        Payment payment = paymentService.processPayment(bookingId, amount, strategy);
        HttpUtils.sendJsonResponse(exchange, 201, payment);
    }

    private PaymentStrategy createPaymentStrategy(String method, Map<String, String> details) {
        switch (method) {
            case "UPI":
                return new UpiPaymentStrategy(details.get("upiId"));
            case "CARD":
                return new CardPaymentStrategy(
                    details.get("cardNumber"),
                    details.get("cvv"),
                    details.get("expiry")
                );
            case "NET_BANKING":
                return new NetBankingStrategy(
                    details.get("bankName"),
                    details.get("accountNumber")
                );
            default:
                throw new IllegalArgumentException("Invalid payment method: " + method);
        }
    }

    private void handleGetPaymentByBooking(HttpExchange exchange, String bookingId) throws IOException {
        Payment payment = paymentService.getPaymentByBooking(bookingId);
        if (payment != null) {
            HttpUtils.sendJsonResponse(exchange, 200, payment);
        } else {
            HttpUtils.sendErrorResponse(exchange, 404, "Payment not found");
        }
    }
}
