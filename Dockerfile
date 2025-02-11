FROM eclipse-temurin:11-jdk-alpine
WORKDIR /app
COPY target/demo-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
