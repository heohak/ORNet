# Stage 1: Build the application
FROM gradle:7.6.0-jdk17-alpine AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the source code to the container
COPY . .

# Build the application and generate the JAR file, skipping tests
RUN gradle build -x test --no-daemon

# Stage 2: Run the application
FROM --platform=linux/amd64 eclipse-temurin:17-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=builder /app/build/libs/bait-0.0.1-SNAPSHOT.jar /app/app.jar

# Copy the application.properties file
COPY src/main/resources/application.properties /app/application.properties

# Set the entry point command to run the JAR file
ENTRYPOINT ["java", "-Dspring.config.location=classpath:/application.properties,file:/app/application.properties", "-jar", "/app/app.jar"]
