FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy all files
COPY . .

# Create bin directory
RUN mkdir -p bin

# Compile Java files
RUN find . -name "*.java" -type f | xargs javac -cp "lib/*" -d bin -encoding UTF-8

# Expose port (Railway will set this via PORT env variable)
EXPOSE 8080

# Run with SSL/TLS configuration for MongoDB Atlas
CMD ["java", "-Djavax.net.ssl.trustStore=/opt/java/openjdk/lib/security/cacerts", "-Djavax.net.ssl.trustStorePassword=changeit", "-Djdk.tls.client.protocols=TLSv1.2", "-cp", "lib/*:bin", "api.ApiServer"]
