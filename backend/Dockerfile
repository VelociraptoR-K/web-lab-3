FROM maven:4.0.0-rc-4-eclipse-temurin-21 AS build
WORKDIR /app

COPY .mvn ./.mvn
COPY pom.xml .
COPY src ./src
COPY mvnw .
RUN chmod +x mvnw

EXPOSE 8080

CMD ["./mvnw", "clean", "generate-sources", "wildfly:dev", "-Dwildfly.serverArgs=-b=0.0.0.0"]