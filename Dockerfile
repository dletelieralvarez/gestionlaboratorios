# Imagen base con Java 
FROM eclipse-temurin:17-jdk-alpine

# Carpeta dentro del contenedor
WORKDIR /app

# Copiamos el JAR 
COPY target/gestionlaboratorios-0.0.1-SNAPSHOT.jar app.jar

# Puerto que expone tu app 
EXPOSE 8080

# Comando para arrancar
ENTRYPOINT ["java", "-jar", "app.jar"]