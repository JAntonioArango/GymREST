FROM openjdk:21-jdk-slim

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY target/eureka-server-1.0.0.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar"]