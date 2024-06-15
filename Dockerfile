# Use a base image with Java 17
FROM openjdk:17-oracle

# Define the working directory inside the container
WORKDIR /app

# Copy the JAR file and the keystore into the container
COPY target/dailygame-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the HTTPS port
EXPOSE 8443

# Command to start your Spring Boot application with the active profile
CMD ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "app.jar"]
