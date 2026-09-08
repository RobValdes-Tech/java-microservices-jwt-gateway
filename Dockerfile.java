# Etapa 1: Entorno de Compilación con Maven
FROM maven:3.8.8-eclipse-temurin-17 AS build-env
WORKDIR /app

# Copiar pom.xml y descargar dependencias para aprovechar la caché de capas de Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código fuente y compilar el archivo JAR de producción saltando tests unitarios
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Entorno de Ejecución ligero con JRE
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build-env /app/target/*.jar app.jar

# Exponer puerto perimetral reactivo de Netty
EXPOSE 5001
ENTRYPOINT ["java", "-jar", "app.jar"]
