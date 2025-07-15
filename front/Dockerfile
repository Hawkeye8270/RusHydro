FROM openjdk:17-jdk-slim
WORKDIR /appFront

COPY target/front-1-SNAPSHOT.jar appFront.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "appFront.jar"]