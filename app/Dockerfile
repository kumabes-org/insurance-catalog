FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace

COPY mvnw pom.xml settings.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw

COPY src src
RUN ./mvnw -s settings.xml -DskipTests package

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]


# java -javaagent:opentelemetry-javaagent.jar \
#      -Dotel.exporter.otlp.endpoint=http://otel-collector:4317 \
#      -Dotel.resource.attributes=service.name=minha-app-java21 \
#      -jar app.jar