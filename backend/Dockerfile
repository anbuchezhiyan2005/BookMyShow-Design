FROM openjdk:17-slim

WORKDIR /app

# Copy all files
COPY . .

# Create bin directory
RUN mkdir -p bin

# Compile Java files
RUN find . -name "*.java" -type f | xargs javac -cp "lib/*" -d bin -encoding UTF-8

# Expose port (Railway will set this via PORT env variable)
EXPOSE 8080

# Run the server
CMD ["java", "-cp", "lib/*:bin", "api.ApiServer"]
