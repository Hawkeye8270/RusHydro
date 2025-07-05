package com.example.configuration;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;


@Configuration
@Slf4j
public class ChromeConfig {

    @Bean
    public WebDriver webDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--remote-debugging-port=9222",
                "--disable-gpu",
                "--window-size=1920,1080"
        );

        // Для работы в контейнере
        options.setBinary("/usr/bin/google-chrome");

        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        log.info("Chrome version: {}", options.getBrowserVersion());

        return new ChromeDriver(options);
    }


    // Универсальный путь для Docker и локальной разработки
    private static final String DRIVER_PATH = isRunningInDocker()
            ? "/usr/local/bin/chromedriver"
            : "src/main/resources/chromedriver";

    public static void setDriverPath() {
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
        log.info("Using chrome driver at: {}", DRIVER_PATH);

        // Проверка существования файла
        File driverFile = new File(DRIVER_PATH);
        if (!driverFile.exists()) {
            log.error("ChromeDriver not found at: {}", driverFile.getAbsolutePath());
            throw new IllegalStateException("ChromeDriver not found at: " + DRIVER_PATH);
        }

        // Проверка прав на выполнение (для Linux)
        if (!driverFile.canExecute() && !System.getProperty("os.name").toLowerCase().contains("win")) {
            log.warn("ChromeDriver is not executable, trying to set permissions...");
            driverFile.setExecutable(true);
        }
    }

    public static ChromeOptions getChromeOptions(boolean headlessMode) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        if (headlessMode || isRunningInDocker()) {
            options.addArguments("--headless");
        } else {
            options.addArguments("start-maximized");
        }
        return options;
    }

    private static boolean isRunningInDocker() {
        return System.getenv("DOCKER_ENV") != null;
    }
}


//  РАБОТАЕТ БЕЗ DOKER
//@Configuration
//@Slf4j
//public class ChromeConfig {
////    public static final String DRIVER_PATH_SHORT = "autoCrawling/src/main/resources/chromedriver.exe";
////    public static final String DRIVER_PATH_SHORT = "autoCrawling/src/main/resources/chromedriver";
//    public static final String DRIVER_PATH_SHORT = "src/main/resources/chromedriver";
////    public static final String DRIVER_PATH_SHORT = "BOOT-INF/classes/chromedriver";
//
//    public static void setDriverPath() {
//        System.setProperty("webdriver.chrome.driver", DRIVER_PATH_SHORT);
//        log.info("Path to chrome driver: {}", System.getProperty("webdriver.chrome.driver"));
//    }
//
//    public static ChromeOptions getChromeOptions(boolean headlessMode) {
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("start-maximized");
//
//        if (headlessMode) {
//            options.addArguments("--headless");
//        }
//        return options;
//    }
//
//}

