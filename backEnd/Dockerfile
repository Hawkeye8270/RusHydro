FROM openjdk:17-jdk-slim
WORKDIR /appBackend

# Замените на имя вашего JAR
COPY target/backEnd-1-SNAPSHOT.jar appBackend.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "appBackend.jar"]


#FROM openjdk:17-jdk-slim
#WORKDIR /app
#
## Замените на имя вашего JAR
#COPY target/backEnd-1-SNAPSHOT.jar app.jar
#
#EXPOSE 8081
#ENTRYPOINT ["java", "-jar", "app.jar"]
