# Utilise une image de base avec Java 17
FROM openjdk:17-oracle

# Définis le répertoire de travail dans le conteneur
WORKDIR /app

# Copie le JAR de ton application dans le conteneur
COPY target/dailygame-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose le port sur lequel ton application Spring Boot écoute
EXPOSE 8080

# Commande pour démarrer ton application Spring Boot
CMD ["java", "-jar", "app.jar"]
