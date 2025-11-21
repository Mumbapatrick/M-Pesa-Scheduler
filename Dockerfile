# Use Eclipse Temurin (official OpenJDK successor)
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and project files
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src ./src

# Give execution permission to gradlew
RUN chmod +x ./gradlew

# Build the Spring Boot jar
RUN ./gradlew bootJar --no-daemon --stacktrace

# Expose the port your app will run on
EXPOSE 8080

# Run the Spring Boot jar dynamically
CMD ["sh", "-c", "java -jar build/libs/*SNAPSHOT.jar"]
