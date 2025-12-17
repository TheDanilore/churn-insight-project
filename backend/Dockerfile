FROM maven:3.9.6-eclipse-temurin-21

WORKDIR /app

# Copiamos el pom.xml y descargamos dependencias (Caché)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el código fuente
COPY src ./src

# Exponemos el puerto de Spring Boot
EXPOSE 8080

# Comando para iniciar en modo desarrollo
CMD ["mvn", "spring-boot:run"]