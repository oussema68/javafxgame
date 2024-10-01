# Build stage
FROM maven:latest AS build

WORKDIR /game

COPY pom.xml .
COPY src ./src

RUN mvn clean package


# Use OpenJDK 21 for the final stage
FROM openjdk:21-jdk-slim



WORKDIR /game

# Copy the tarball into the container
COPY myapp.tar /app/myapp.tar

# Extract the tarball
RUN tar -xf myapp.tar && rm myapp.tar

# Run the application with JavaFX modules
ENTRYPOINT ["java", "--module-path", "/game", "--add-modules", "javafx.controls,javafx.fxml,javafx.graphics,javafx.base", "-jar", "myapp-1-jar-with-dependencies.jar"]