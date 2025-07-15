FROM openjdk:17-jdk-slim

# Установка postgresql-client-16, совместимого с сервером
RUN apt-get update && \
    apt-get install -y wget gnupg2 lsb-release && \
    sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list' && \
    wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | apt-key add - && \
    apt-get update && \
    apt-get install -y postgresql-client-16 && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /appBackend

COPY target/backEnd-1-SNAPSHOT.jar appBackend.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "appBackend.jar"]
