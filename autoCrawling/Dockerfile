# Этап сборки
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app

# Копируем только необходимые файлы
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


## Этап сборки
#FROM maven:3.8.6-openjdk-17-slim AS build
#WORKDIR /app
#COPY pom.xml .
#RUN mvn dependency:go-offline
#COPY src ./src
#RUN mvn clean package -DskipTests
#
## Финальный образ
#FROM openjdk:17-jdk-slim
#WORKDIR /appAutoCrawling
#COPY --from=build /app/target/autoCrawling-*.jar appAutoCrawling.jar
#
## Переменные среды
#ENV SELENIUM_REMOTE_URL=http://selenium-hub:4444/wd/hub
#ENV SHM_SIZE=2g
#
## Точка входа
#ENTRYPOINT ["java", "-jar", "appAutoCrawling.jar"]



## Используем официальный образ OpenJDK
#FROM openjdk:17-jdk-slim
#
## Устанавливаем переменные среды
#ENV SELENIUM_REMOTE_URL=http://selenium-hub:4444/wd/hub
#
## Копируем исходный код и зависимости
#COPY pom.xml .
#COPY src ./src
#
### Устанавливаем Maven (если используется Maven)
##RUN apt-get update && apt-get install -y maven
#
### Собираем проект
##RUN mvn clean package -DskipTests
#
### Запускаем тесты (или приложение)
##CMD ["mvn", "test"]
#
## Копирование приложения
#COPY target/autoCrawling-1-SNAPSHOT.jar appAutoCrawling.jar
#
## Настройки
#ENV SHM_SIZE=2g
#EXPOSE 8082
#
## Запуск приложения
#ENTRYPOINT ["java", "-jar", "appAutoCrawling.jar"]



###
 # REST OF YOUR DOKCERFILE HERE
###


#FROM openjdk:17-jdk-slim
#
#WORKDIR /appAutoCrawling
#
## Установка зависимостей с фиксированными версиями
#RUN apt-get update && \
#    apt-get install -y \
#    wget \
#    gnupg \
#    unzip \
#    libxss1 \
#    libnspr4 \
#    libnss3 \
#    libgbm1 \
#    fonts-liberation \
#    xdg-utils \
#    --no-install-recommends && \
#    apt-get clean && \
#    rm -rf /var/lib/apt/lists/*
#
## Установка конкретной версии Chrome
#RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - && \
#    echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list && \
#    apt-get update && \
#    apt-get install -y google-chrome-stable=115.0.5790.102-1 \
#    --no-install-recommends && \
#    rm -rf /var/lib/apt/lists/*
#
## Копирование ChromeDriver
#COPY src/main/resources/chromedriver /usr/local/bin/chromedriver
#RUN chmod +x /usr/local/bin/chromedriver
#
## Проверка версий
#RUN google-chrome --version && \
#    /usr/local/bin/chromedriver --version
#
#COPY target/autoCrawling-1-SNAPSHOT.jar appAutoCrawling.jar
#
## Увеличение shared memory
#ENV SHM_SIZE=2g
#
#EXPOSE 8082
#
#ENTRYPOINT ["java", "-jar", "appAutoCrawling.jar"]


#// РАБОТАЕТ БЕЗ DOCKER
#FROM openjdk:17-jdk-slim
#WORKDIR /appAutoCrawling
#
#COPY target/autoCrawling-1-SNAPSHOT.jar appAutoCrawling.jar
#
#EXPOSE 8082
#ENTRYPOINT ["java", "-jar", "appAutoCrawling.jar"]