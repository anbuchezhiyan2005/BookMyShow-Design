package config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.concurrent.TimeUnit;

public class MongoDBConnectionManager {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    private MongoDBConnectionManager() {}

    public static void initialize(String connectionString, String databaseName) {
        if (mongoClient == null) {
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(connectionString))
                    .applyToConnectionPoolSettings(builder -> 
                        builder.maxSize(50)
                               .minSize(5)
                               .maxWaitTime(120, TimeUnit.SECONDS)
                    )
                    .build();

            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase(databaseName);
            System.out.println("MongoDB connected successfully to database: " + databaseName);
        }
    }

    public static MongoDatabase getDatabase() {
        if (database == null) {
            throw new IllegalStateException("MongoDB not initialized. Call initialize() first.");
        }
        return database;
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
            System.out.println("MongoDB connection closed successfully.");
        }
    }
}
