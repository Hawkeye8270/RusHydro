package com.example.service;


import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.*;

public class PostgresDumpService {
    private final String dockerContainerName;
    private final String dbUser;
    private final String dbPassword;
    private final String dbName;
    private final String savePath;

    public PostgresDumpService(String dockerContainerName, String dbUser,
                          String dbPassword, String dbName, String savePath) {
        this.dockerContainerName = dockerContainerName;
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

        Files.createDirectories(dumpPath.getParent());

        // Команды для выполнения в контейнере
        executeDockerCommand(new String[]{
                "docker", "exec", dockerContainerName,
                "pg_dump",
                "-U", dbUser,
                "-d", dbName,
                "-f", "/tmp/" + dumpFileName
        });

        // Копируем файл из контейнера
        executeCommand(new String[]{
                "docker", "cp",
                dockerContainerName + ":/tmp/" + dumpFileName,
                dumpPath.toString()
        });

        // Удаляем временный файл в контейнере
        executeDockerCommand(new String[]{
                "docker", "exec", dockerContainerName,
                "rm", "/tmp/" + dumpFileName
        });

        System.out.println("Dump successfully created at: " + dumpPath);
    }

    private void executeDockerCommand(String[] command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.environment().put("PGPASSWORD", dbPassword);
        int exitCode = pb.start().waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Command failed with exit code: " + exitCode);
        }
    }

    private void executeCommand(String[] command) throws IOException, InterruptedException {
        int exitCode = new ProcessBuilder(command).start().waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Command failed with exit code: " + exitCode);
        }
    }
}