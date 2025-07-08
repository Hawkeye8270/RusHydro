package com.example.configuration;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;


@Configuration
@Slf4j
public class ChromeConfig {

    @Bean
    public ChromeOptions chromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--headless=new",
                "--window-size=1920,1080",
                "--remote-allow-origins=*"
        );
        return options;
    }

    @Bean(destroyMethod = "quit")
    public RemoteWebDriver remoteWebDriver(ChromeOptions options) throws MalformedURLException {
        String hubUrl = System.getenv().getOrDefault(
                "SELENIUM_REMOTE_URL",
                "http://selenium-hub:4444/wd/hub"
        );
        log.info("Connecting to Selenium Hub: {}", hubUrl);
        RemoteWebDriver driver = new RemoteWebDriver(new URL(hubUrl), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        return driver;
    }

    @Bean
    public WebDriverWait webDriverWait(RemoteWebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(30));
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

