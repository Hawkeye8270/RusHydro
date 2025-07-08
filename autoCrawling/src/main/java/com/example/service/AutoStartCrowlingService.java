package com.example.service;

import com.example.repository.DataDBRepository;
import com.example.repository.DataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class AutoStartCrowlingService {

    private static final String URL_TEST = "https://rushydro.ru/informer/";
    private static SeleniumDataService seleniumDS;

    @Autowired
    public AutoStartCrowlingService(SeleniumDataService seleniumDS, DataDBRepository dataDBRepository) {
        this.seleniumDS = seleniumDS;
        this.dataDBRepository = dataDBRepository;
    }

    private static DataDBRepository dataDBRepository;

    public AutoStartCrowlingService(DataDBRepository dataDBRepository) {
        this.dataDBRepository = dataDBRepository;
    }

    public void autoStartCrowling(String river, String ges, LocalDate date) {
        try {
            String year = String.valueOf(date.getYear());
            String month = getRussianMonthName(date.getMonth());
            String day = String.valueOf(date.getDayOfMonth());

            seleniumDS.start(true);
            seleniumDS.openPage(URL_TEST);
            closeCookie();
            setRiver(river);
            setGes(ges);
            setPeriodOfDays();
            setMonthAndYearAndDate(year, month, day);
            DataRepository.createNewData(river, ges, collectionDataFromGes(ges, river, year));

            seleniumDS.waitSomeTime(2000);
        } catch (Exception ex) {
            log.error("Ошибка при выполнении краулинга: {}", ex.getMessage());
        } finally {
            seleniumDS.stop();
        }
    }

    private String getRussianMonthName(Month month) {
        return switch (month) {
            case JANUARY -> "Январь";
            case FEBRUARY -> "Февраль";
            case MARCH -> "Март";
            case APRIL -> "Апрель";
            case MAY -> "Май";
            case JUNE -> "Июнь";
            case JULY -> "Июль";
            case AUGUST -> "Август";
            case SEPTEMBER -> "Сентябрь";
            case OCTOBER -> "Октябрь";
            case NOVEMBER -> "Ноябрь";
            case DECEMBER -> "Декабрь";
        };
    }

    private static void setRiver(String river) {
        try {
            String xpath = "//li[text()='" + river + "']";
            seleniumDS.click(seleniumDS.getElem(ElementXPath.SELECT_RIVER));
            seleniumDS.click(seleniumDS.getElem(xpath));
        } catch (Exception ex) {
            log.info("Ошибка при выборе реки: {}", ex.getMessage());
        }
    }

    private static void setGes(String ges) {
        try {
            String xpath = "//li[text()='" + ges + "']";
            seleniumDS.click(seleniumDS.getElem(ElementXPath.SELECT_GES));
            seleniumDS.click(seleniumDS.getElem(xpath));
        } catch (Exception ex) {
            log.info("Ошибка при выборе ГЭС: {}", ex.getMessage());
        }
    }

    private static void closeCookie() {
        try {
            seleniumDS.click(seleniumDS.getElem(ElementXPath.CLOSE_COOKIE));
        } catch (Exception ex) {
            log.info("Ошибка при закрытии cookie: {}", ex.getMessage());
        }
    }

    private static void setPeriodOfDays() {
        try {
            seleniumDS.click(seleniumDS.getElem(ElementXPath.SELECT_PERIOD_OF_DAYS));
        } catch (Exception ex) {
            log.info("Ошибка при выборе периуда дат: {}", ex.getMessage());
        }
    }

    private static void setMonthAndYearAndDate(String year, String month, String day) {
        try {
            seleniumDS.findDate(year, month, day);
        } catch (Exception ex) {
            log.info("Ошибка при изменении месяца и года: {}", ex.getMessage());
        }
    }

    private static Map<Date, Float> collectionDataFromGes(String ges, String river, String year) {
        try {
            return seleniumDS.collectionData(ges, river, year);
        } catch (Exception ex) {
            log.info("Ошибка при считывании данных об уровне: {}", ex.getMessage());
            return null;
        }
    }
}
