package com.example.configuration;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.net.URL;


@Configuration
@Slf4j
public class ChromeConfig {

    public static ChromeOptions getChromeOptions(boolean headlessMode) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox"); // Обязательно для Docker
        options.addArguments("--disable-dev-shm-usage"); // Обязательно для Docker
        options.addArguments("start-maximized");

        if (headlessMode) {
            options.addArguments("--headless=new"); // Новый headless-режим Chrome
        }
        return options;
    }

//    @Bean
//    public WebDriver webDriver() {
//        ChromeOptions options = getChromeOptions(true); // headless=true для Docker
//
//        // Используем RemoteWebDriver вместо локального
//        try {
//            String seleniumHubUrl = System.getenv().getOrDefault(
//                    "SELENIUM_REMOTE_URL",
//                    "http://localhost:4444/wd/hub" // fallback для локального тестирования
//            );
//            log.info("Connecting to Selenium Hub: {}", seleniumHubUrl);
//            return new RemoteWebDriver(new URL(seleniumHubUrl), options);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to initialize RemoteWebDriver", e);
//        }
//    }
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

