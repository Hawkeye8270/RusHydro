package com.example.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class SeleniumDataService {

    private final RemoteWebDriver driver;
    private final WebDriverWait wait;

    public SeleniumDataService(RemoteWebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        log.info("SeleniumDataService initialized with driver: {}", driver);
    }


    public boolean start(boolean headlessMode) {
        try {
            driver.get("about:blank"); // Тестовая загрузка страницы
            return true;
        } catch (Exception ex) {
            log.error("WebDriver initialization failed", ex);
            return false;
        }
    }

    public boolean openPage(String url) {
        try {
            driver.get(url);
//            remoteWebDriver.get(url);
            log.info("Open page: {}", url);
        } catch (Exception ex) {
            log.error("open page problem: {}", ex.getMessage());
            return false;
        }
        return true;
    }

    public void stop() {
        if (driver != null) {
            driver.quit();
//        if (remoteWebDriver != null) {
//            remoteWebDriver.quit();
        }
    }

    public WebElement getElem(String xpath) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
    }

    public WebElement getChildElem(String xpath, WebElement parentElem) {
        return wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parentElem, By.xpath(xpath)));
    }

    public String getChildElemText(String xpath, WebElement parentElem) {
        try {
            return getChildElem(xpath, parentElem).getText();
        } catch (Exception ex) {
            log.warn("Child element not located: {}", ex.getMessage());
            return "";
        }
    }

    public void click(WebElement webElem) throws InterruptedException {
        Thread.sleep(500);
        ((JavascriptExecutor) driver).executeScript(
//        ((JavascriptExecutor) remoteWebDriver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", webElem);
    }

    public void waitSomeTime(int milliSec) {
        try {
            Thread.sleep(milliSec);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public void findDate(String year, String month, String day) throws InterruptedException {
        WebElement dateInput2 = driver.findElement(By.id("water-date"));
//        WebElement dateInput2 = remoteWebDriver.findElement(By.id("water-date"));
        dateInput2.click();

        WebElement datepickerTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.datepicker--nav-title")));
        String titleText = datepickerTitle.getText();
        String currentYear = titleText.replaceAll("[^0-9]", "");

        if (!currentYear.equals(year)) {
            boolean flag = false;
            String yearAfterPrevious = "";

            while (!flag) {
                WebElement arrowLeft = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[@class='datepicker--nav-action' and @data-action='prev']")));
                arrowLeft.click();

                WebElement tempYearWb = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.datepicker--nav-title")));
                String tempYearTitle = tempYearWb.getText();
                yearAfterPrevious = tempYearTitle.replaceAll("[^0-9]", "");

                flag = (yearAfterPrevious.equals(year));
            }
        }

        WebElement datepickerTitle2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.datepicker--nav-title")));
        String titleText2 = datepickerTitle2.getText();
        String currentMonth = titleText2.replaceAll("[^\\p{L}]", "");

        if (!currentMonth.equals(month)) {
            boolean flag = false;
            String monthAfterPrevious = "";

            while (!flag) {
                WebElement arrowLeft = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[@class='datepicker--nav-action' and @data-action='prev']")));
                arrowLeft.click();

                WebElement tempMonthWb = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.datepicker--nav-title")));
                String tempMonthTitle = tempMonthWb.getText();
                monthAfterPrevious = tempMonthTitle.replaceAll("[^\\p{L}]", "");

                flag = (monthAfterPrevious.equals(month));
            }
        }

        String xpath = "//div[(@class='datepicker--cell datepicker--cell-day' or @class='datepicker--cell datepicker--cell-day -weekend-') " +
                "and @data-date='" + day + "']";

        Thread.sleep(1000);
        driver.findElement(By.xpath(xpath)).click();
//        remoteWebDriver.findElement(By.xpath(xpath)).click();
    }

    public Map<Date, Float> collectionData(String ges, String river, String year) throws ParseException {
        String water_level_Str = "";
        String water_date_Str = "";

        try {
            Thread.sleep(3000);
            List<WebElement> secondAttempt = driver.findElements(By.tagName("option"));
//            List<WebElement> secondAttempt = remoteWebDriver.findElements(By.tagName("option"));

            for (WebElement option : secondAttempt) {
                if (ges.equals(option.getText())) {
                    water_level_Str = option.getAttribute("water-level");
                    water_date_Str = option.getAttribute("water-date");
                    break;
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        String[] water_level = water_level_Str.split(",");
        String[] date = water_date_Str.split(",");

        Map<Date, Float> mapOfData = new TreeMap<>();

        for (int i = 0; i < water_level.length; i++) {
            String tempDate = date[i];
            String fullTempDate = tempDate + "." + year;
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Float tempLevel = Float.parseFloat(water_level[i]);

            try {
                Date parsedDate = sdf.parse(fullTempDate);
                mapOfData.put(parsedDate, tempLevel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        SimpleDateFormat storageFormat = new SimpleDateFormat("dd.MM.yyyy");
//        System.out.println("-------------------------------------------------------------");
//        System.out.println("Данные по реке - " + river + ". ГЭС - " + ges + ".");
//        mapOfData.forEach((key, value) -> System.out.println("Date " + storageFormat.format(key) + " - " + "Level " + value));

        return mapOfData;
    }


}
