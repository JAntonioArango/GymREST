# 1. Build stage
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace
COPY .mvn/         .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw --batch-mode dependency:go-offline
COPY src/ src/
RUN ./mvnw --batch-mode -DskipTests package

# 2. Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/target/*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]

