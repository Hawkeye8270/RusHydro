package com.example.service;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PostgresDumpService {

    private final String dbHost;
    private final String dbPort;
    private final String dbUser;
    private final String dbPassword;
    private final String dbName;
    private final String savePath;

    public PostgresDumpService(String dbHost, String dbPort, String dbUser,
                          String dbPassword, String dbName, String savePath) {
        this.dbHost = dbHost;
        this.dbPort = dbPort;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        this.dbName = dbName;
        this.savePath = savePath;
    }

    public void createDump() throws IOException, InterruptedException {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String dumpFileName = "postgres_dump_" + timestamp + ".sql";
        Path dumpPath = Paths.get(savePath, dumpFileName);

        // Создаем директорию, если не существует
        Files.createDirectories(dumpPath.getParent());

        // Формируем команду pg_dump
        String[] pgDumpCommand = {
                "pg_dump",
                "-h", dbHost,
                "-p", dbPort,
                "-U", dbUser,
                "-d", dbName,
                "-f", dumpPath.toString()
        };

        // Выполняем команду
        executeCommand(pgDumpCommand);

        System.out.println("Dump successfully created at: " + dumpPath);
    }

    private void executeCommand(String[] command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);

        // Устанавливаем переменные окружения
        pb.environment().put("PGPASSWORD", dbPassword);

        // Перенаправляем stderr в stdout для удобства отладки
        pb.redirectErrorStream(true);

        Process process = pb.start();

        // Читаем вывод команды (для отладки)
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[pg_dump] " + line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("pg_dump failed with exit code: " + exitCode);
        }
    }

}