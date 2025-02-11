FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/stiven-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
