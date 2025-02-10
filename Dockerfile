# Imagen base con JDK 17
FROM openjdk:17-jdk-slim

# Directorio de trabajo dentro del contenedor
WORKDIR /servicio_blade

# Copiar archivos de la aplicación
COPY target/stiven-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

# Exponer el puerto de la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
