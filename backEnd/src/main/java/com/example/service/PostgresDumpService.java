package com.example.service;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;

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

        Files.createDirectories(dumpPath.getParent());

        String[] pgDumpCommand = {
                "pg_dump",
                "-h", dbHost,
                "-p", dbPort,
                "-U", dbUser,
                "-d", dbName,
                "-f", dumpPath.toString()
        };

        executeCommand(pgDumpCommand);

        System.out.println("Dump successfully created at: " + dumpPath);

        deletePreviousDump(dumpPath);
    }

    private void deletePreviousDump(Path currentDumpPath) throws IOException {
        File dumpDir = new File(savePath);
        if (!dumpDir.exists() || !dumpDir.isDirectory()) {
            return;
        }

        File[] dumpFiles = dumpDir.listFiles((dir, name) ->
                name.startsWith("postgres_dump_") &&
                        name.endsWith(".sql") &&
                        !name.equals(currentDumpPath.getFileName().toString())
        );

        if (dumpFiles == null || dumpFiles.length == 0) {
            return;
        }

        Arrays.sort(dumpFiles, Comparator.comparingLong(File::lastModified));

        for (File oldDump : dumpFiles) {
            try {
                Files.delete(oldDump.toPath());
                System.out.println("Deleted old dump file: " + oldDump.getName());
            } catch (IOException e) {
                System.err.println("Failed to delete old dump file: " +
                        oldDump.getName() + ": " + e.getMessage());
            }
        }
    }

    private void executeCommand(String[] command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);

        pb.environment().put("PGPASSWORD", dbPassword);

        pb.redirectErrorStream(true);

        Process process = pb.start();

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