package com.example.configuration;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ChromeConfig {
//    public static final String DRIVER_PATH_SHORT = "src/main/resources/chromedriver.exe";
    public static final String DRIVER_PATH_SHORT = "autoCrawling/src/main/resources/chromedriver.exe";

    public static void setDriverPath() {
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH_SHORT);
        log.info("Path to chrome driver: {}", System.getProperty("webdriver.chrome.driver"));
    }

    public static ChromeOptions getChromeOptions(boolean headlessMode) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");

        if (headlessMode) {
            options.addArguments("--headless");
        }
        return options;
    }

}

