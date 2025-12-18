package api;

import com.sun.net.httpserver.HttpServer;
import config.MongoDBConnectionManager;
import api.servlets.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ApiServer {
    // Use PORT from environment variable (Railway sets this) or default to 8080
    private static final int PORT = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
    private HttpServer server;

    public void start() throws IOException {
        // Initialize MongoDB connection
        // Use MONGODB_URI from environment or default to local
        String mongoUri = System.getenv().getOrDefault("MONGODB_URI", "mongodb://localhost:27017");
        MongoDBConnectionManager.initialize(mongoUri, "bookmyshow");

        // Create HTTP server
        server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Register API endpoints
        server.createContext("/api/users", new UserHandler());
        server.createContext("/api/movies", new MovieHandler());
        server.createContext("/api/theatres", new TheatreHandler());
        server.createContext("/api/shows", new ShowHandler());
        server.createContext("/api/seats", new SeatHandler());
        server.createContext("/api/bookings", new BookingHandler());
        server.createContext("/api/payments", new PaymentHandler());
        server.createContext("/api/receipts", new ReceiptHandler());

        // Set executor (null means default executor)
        server.setExecutor(null);
        
        // Start server
        server.start();
        System.out.println("🚀 BookMyShow API Server started on http://localhost:" + PORT);
        System.out.println("📡 API endpoints available at http://localhost:" + PORT + "/api");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            MongoDBConnectionManager.close();
            System.out.println("Server stopped");
        }
    }

    public static void main(String[] args) {
        try {
            ApiServer apiServer = new ApiServer();
            apiServer.start();
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
