# Etapa 1: Compilar
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copiar Maven wrapper y pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Descargar dependencias
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src src

# Compilar (sin tests)
RUN ./mvnw clean package -DskipTests

# Etapa 2: Ejecutar
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar JAR compilado
COPY --from=build /app/target/*.jar app.jar

# Puerto
EXPOSE 8080

# Ejecutar
ENTRYPOINT ["java", "-jar", "app.jar"]
