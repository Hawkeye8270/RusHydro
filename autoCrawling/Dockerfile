# Этап сборки
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

# Собираем проект (без пропуска главного класса)
RUN mvn clean package -DskipTests

# Финальный образ
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Копируем собранный JAR (учитываем finalName из pom.xml)
COPY --from=build /app/target/autoCrawling*.jar app.jar

# Переменные среды
ENV SELENIUM_REMOTE_URL=http://selenium-hub:4444/wd/hub
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/rushydro
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=123

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]