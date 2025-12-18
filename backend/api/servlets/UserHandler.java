package api.servlets;

import api.HttpUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import models.User;
import repositories.UserRepositoryImpl;
import services.UserService;

import java.io.IOException;
import java.util.Map;

public class UserHandler implements HttpHandler {
    private final UserService userService;

    public UserHandler() {
        this.userService = new UserService(new UserRepositoryImpl());
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (HttpUtils.handleCors(exchange)) return;

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if (method.equals("POST") && path.endsWith("/register")) {
                handleRegister(exchange);
            } else if (method.equals("POST") && path.endsWith("/login")) {
                handleLogin(exchange);
            } else if (method.equals("GET")) {
                String userId = HttpUtils.extractPathParam(path, "/api/users");
                if (userId != null) {
                    handleGetUser(exchange, userId);
                } else {
                    HttpUtils.sendErrorResponse(exchange, 400, "User ID required");
                }
            } else {
                HttpUtils.sendErrorResponse(exchange, 405, "Method not allowed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpUtils.sendErrorResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleRegister(HttpExchange exchange) throws IOException {
        User user = HttpUtils.readRequestBody(exchange, User.class);
        
        User registered = userService.registerUser(
            user.getName(),
            user.getEmail(),
            user.getPassword(),
            user.getPhoneNumber()
        );
        HttpUtils.sendJsonResponse(exchange, 201, registered);
    }

    private void handleLogin(HttpExchange exchange) throws IOException {
        Map<String, String> credentials = HttpUtils.readRequestBody(exchange, Map.class);
        String email = credentials.get("email");
        String password = credentials.get("password");
        
        User user = userService.loginUser(email, password);
        if (user != null) {
            HttpUtils.sendJsonResponse(exchange, 200, user);
        } else {
            HttpUtils.sendErrorResponse(exchange, 401, "Invalid email or password");
        }
    }

    private void handleGetUser(HttpExchange exchange, String userId) throws IOException {
        User user = userService.getUserById(userId);
        if (user != null) {
            HttpUtils.sendJsonResponse(exchange, 200, user);
        } else {
            HttpUtils.sendErrorResponse(exchange, 404, "User not found");
        }
    }
}
