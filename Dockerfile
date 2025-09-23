# --- Etapa 1: Construcción con Maven ---
# Usamos una imagen de Maven con Java 17 para compilar el proyecto
FROM maven:3.8.5-openjdk-17 AS build

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el pom.xml para descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el resto del código fuente y construimos el .jar
COPY src ./src
RUN mvn package -DskipTests

# --- Etapa 2: Ejecución ---
# Usamos una imagen ligera solo con el entorno de ejecución de Java 17
FROM eclipse-temurin:17-jre-focal

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el .jar construido en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto en el que corre la aplicación
EXPOSE 8081

# Comando para ejecutar la aplicación cuando se inicie el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]
