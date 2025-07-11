package com.example;

import com.example.service.PostgresDumpService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


//@EnableScheduling         // АВТОСБОРЩИК ДЛЯ БД
@EnableJpaRepositories
@SpringBootApplication
@EntityScan("com.example.entity")
public class AppMVC {

//    private static final String DOCKER_CONTAINER = "db"; // Из connection.url (jdbc:postgresql://db:5432/...)
//    private static final String DB_USER = "postgres";    // Из connection.username
//    private static final String DB_PASSWORD = "123";    // Из connection.password
//    private static final String DB_NAME = "rushydro";   // Из connection.url (jdbc:postgresql://.../rushydro)
//    private static final String SAVE_PATH = "D:\\Temp\\"; // Путь для сохранения дампов
////    private static final int DUMP_INTERVAL_HOURS = 3;   // Интервал в часах (оставляем как было)
//    private static final int DUMP_INTERVAL_MINUTES = 2;   // Интервал в минутах

    private static final String DB_HOST = "db"; // из вашего connection.url
    private static final String DB_PORT = "5432"; // стандартный порт PostgreSQL
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "123";
    private static final String DB_NAME = "rushydro";
    private static final String SAVE_PATH = "/app/dumps/"; // путь внутри контейнера
    private static final int DUMP_INTERVAL_HOURS = 1;
//    private static final int DUMP_INTERVAL_MINUTES = 2;   // Интервал в минутах

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(AppMVC.class, args);


        // Инициализация dumper
        PostgresDumpService dumper = new PostgresDumpService(
                DB_HOST,
                DB_PORT,
                DB_USER,
                DB_PASSWORD,
                DB_NAME,
                SAVE_PATH
        );

        // Настройка периодического выполнения
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(
                () -> {
                    try {
                        System.out.println("Starting database dump...");
                        dumper.createDump();
                    } catch (Exception e) {
                        System.err.println("Error creating dump: " + e.getMessage());
                        e.printStackTrace();
                    }
                },
                0, // начальная задержка
                DUMP_INTERVAL_HOURS,
                TimeUnit.HOURS
//                DUMP_INTERVAL_MINUTES,
//                TimeUnit.MINUTES
        );

    }


}
