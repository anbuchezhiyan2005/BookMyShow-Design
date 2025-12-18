FROM amazoncorretto:17

WORKDIR /app

# Install ca-certificates for SSL
RUN yum install -y ca-certificates && yum clean all

# Copy all files
COPY . .

# Create bin directory
RUN mkdir -p bin

# Compile Java files
RUN find . -name "*.java" -type f | xargs javac -cp "lib/*" -d bin -encoding UTF-8

# Expose port (Railway will set this via PORT env variable)
EXPOSE 8080

# Run with SSL/TLS configuration for MongoDB Atlas
CMD ["java", "-Dcom.amazonaws.sdk.disableCertChecking=false", "-cp", "lib/*:bin", "api.ApiServer"]
