FROM openjdk:17-jdk-slim
WORKDIR /appAutoCrawling

# Замените на имя вашего JAR
COPY target/autoCrawling-1-SNAPSHOT.jar appAutoCrawling.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "appAutoCrawling.jar"]