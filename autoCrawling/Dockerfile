FROM openjdk:17-jdk-slim

WORKDIR /appAutoCrawling

# Установка зависимостей с фиксированными версиями
RUN apt-get update && \
    apt-get install -y \
    wget \
    gnupg \
    unzip \
    libxss1 \
    libnspr4 \
    libnss3 \
    libgbm1 \
    fonts-liberation \
    xdg-utils \
    --no-install-recommends && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Установка конкретной версии Chrome
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && \
    apt-get install -y google-chrome-stable=115.0.5790.102-1 \
    --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*

# Копирование ChromeDriver
COPY src/main/resources/chromedriver /usr/local/bin/chromedriver
RUN chmod +x /usr/local/bin/chromedriver

# Проверка версий
RUN google-chrome --version && \
    /usr/local/bin/chromedriver --version

COPY target/autoCrawling-1-SNAPSHOT.jar appAutoCrawling.jar

# Увеличение shared memory
ENV SHM_SIZE=2g

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "appAutoCrawling.jar"]


#// РАБОТАЕТ БЕЗ DOCKER
#FROM openjdk:17-jdk-slim
#WORKDIR /appAutoCrawling
#
#COPY target/autoCrawling-1-SNAPSHOT.jar appAutoCrawling.jar
#
#EXPOSE 8082
#ENTRYPOINT ["java", "-jar", "appAutoCrawling.jar"]