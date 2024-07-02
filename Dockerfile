FROM --platform=linux/amd64 eclipse-temurin:17-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file and application properties
COPY build/libs/bait-0.0.1-SNAPSHOT.jar /app/app.jar
COPY src/main/resources/application.properties /app/application.properties

# Use ENTRYPOINT for the main command
ENTRYPOINT ["java", "-Dspring.config.location=classpath:/application.properties,file:/app/application.properties", "-jar", "/app/app.jar"]

