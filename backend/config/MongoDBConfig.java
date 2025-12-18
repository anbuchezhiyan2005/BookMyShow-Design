package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MongoDBConfig {
    private Properties properties;

    public MongoDBConfig() {
        properties = new Properties();
    }

    public void loadFromProperties(String filepath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filepath)) {
            properties.load(fis);
        }
    }

    public void loadFromResource(String resourcePath) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            properties.load(is);
        }
    }

    public String getConnectionString() {
        return properties.getProperty("connection.string", "mongodb://localhost:27017");
    }

    public String getDatabaseName() {
        return properties.getProperty("database.name", "bookmyshow");
    }

    public int getPoolMaxSize() {
        return Integer.parseInt(properties.getProperty("pool.maxSize", "50"));
    }

    public int getPoolMinSize() {
        return Integer.parseInt(properties.getProperty("pool.minSize", "5"));
    }

    public long getMaxWaitTime() {
        return Long.parseLong(properties.getProperty("pool.maxWaitTime", "120000"));
    }

    public long getMaxConnectionLifeTime() {
        return Long.parseLong(properties.getProperty("pool.maxConnectionLifeTime", "0"));
    }

    public long getMaxConnectionIdleTime() {
        return Long.parseLong(properties.getProperty("pool.maxConnectionIdleTime", "0"));
    }
}
