package com.example.service;

//
//import com.example.demo.repository.DataDBRepository;
//import com.example.demo.repository.DataRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Map;
//
//@Service
//@Slf4j
//public class SeleniumRequestDataService {
//    private static final String URL_TEST = "https://rushydro.ru/informer/";
//    private static SeleniumDataService seleniumDS;
//
//    @Autowired
//    public SeleniumRequestDataService(SeleniumDataService seleniumDS, DataDBRepository dataDBRepository) {
//        this.seleniumDS = seleniumDS;
//        this.dataDBRepository = dataDBRepository;
//    }
//
//    private static DataRepository dataRep;
//
//    public SeleniumRequestDataService(DataRepository dataRep, DataDBRepository dataDBRepository) {
//        SeleniumRequestDataService.dataRep = dataRep;
//        this.dataDBRepository = dataDBRepository;
//    }
//
////    @Autowired
////    private static DataDBRepository dataDBRepository;
//
//    private static DataDBRepository dataDBRepository;
//
//    public SeleniumRequestDataService(DataDBRepository dataDBRepository) {
//        this.dataDBRepository = dataDBRepository;
//    }
//    //    private SeleniumHandler seleniumHandler = new SeleniumHandler();
//
//
////    private void startApp(){
////        seleniumDS.start(false);
////        seleniumDS.waitSomeTime(1000);
////        seleniumDS.openPage(URL_TEST);
////        log.info("Open page: {}", URL_TEST);
////
////
//////        if (seleniumHandler.start(false)){
//////            seleniumHandler.openPage(URL_TEST);
//////            log.info("Open page: {}", URL_TEST);
//////        }
////    }
//
////    static String ges = "Рыбинская";
////    static String river = "Волга";
////    static String year = "2025";
////    static String month = "Март";
////    static String day = "15";
//
//
//    public static void startCrowling(String river, String ges, String year, String month, String day) {
//        try {
//            seleniumDS.start(false);
//            seleniumDS.openPage(URL_TEST);
//            closeCookie();
//            setRiver(river);
//            setGes(ges);
//            setPeriodOfDays();
//            setMonthAndYearAndDate(year, month, day);
//
//            DataRepository.createNewData(river, ges, collectionDataFromGes(ges, river, year));
//
//            seleniumDS.waitSomeTime(2000);
////            seleniumDS.stop();
//
//        } catch (Exception ex) {
//            ex.getMessage();
//        } finally {
//            seleniumDS.stop();
//        }
//    }
//
//    public static void requestDataFromDB(String river, String ges, String dateStartStr, String dateFinisStr) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        sdf.setLenient(false);
//        Date dateStart = sdf.parse(dateStartStr);
//        Date dateFinish = sdf.parse(dateFinisStr);
//
//        dataDBRepository.findByRiverAndGesAndDateBetween(river, ges, dateStart, dateFinish);
//    }
//
////    public static void startCrowling() {
////        seleniumDS.start(false);
////        seleniumDS.openPage(URL_TEST);
////        closeCookie();
////        setRiver(river);
////        setGes(ges);
////        setPeriodOfDays();
////        setMonthAndYearAndDate(year, month, day);
////        collectionDataFromGes(ges, river, year);
////
////        seleniumDS.waitSomeTime(2000);
////        seleniumDS.stop();
////
////    }
//
//    private static void setRiver(String river) {
//        try {
//            String xpath = "//li[text()='" + river + "']";
//            seleniumDS.click(seleniumDS.getElem(ElementXPath.SELECT_RIVER));
//            seleniumDS.click(seleniumDS.getElem(xpath));
//        } catch (Exception ex) {
//            log.info("Ошибка при выборе реки: {}", ex.getMessage());
//        }
//    }
//
//    private static void setGes(String ges) {
//        try {
//            String xpath = "//li[text()='" + ges + "']";
//            seleniumDS.click(seleniumDS.getElem(ElementXPath.SELECT_GES));
//            seleniumDS.click(seleniumDS.getElem(xpath));
//        } catch (Exception ex) {
//            log.info("Ошибка при выборе ГЭС: {}", ex.getMessage());
//        }
//    }
//
//    private static void closeCookie() {
//        try {
//            seleniumDS.click(seleniumDS.getElem(ElementXPath.CLOSE_COOKIE));
//        } catch (Exception ex) {
//            log.info("Ошибка при закрытии cookie: {}", ex.getMessage());
//        }
//    }
//
//    private static void setPeriodOfDays() {
//        try {
//            seleniumDS.click(seleniumDS.getElem(ElementXPath.SELECT_PERIOD_OF_DAYS));
//        } catch (Exception ex) {
//            log.info("Ошибка при выборе периуда дат: {}", ex.getMessage());
//        }
//    }
//
//    private static void setMonthAndYearAndDate(String year, String month, String day) {
//        try {
//            seleniumDS.findDate(year, month, day);
//        } catch (Exception ex) {
//            log.info("Ошибка при изменении месяца и года: {}", ex.getMessage());
//        }
//    }
//
//    private static Map<Date, Float> collectionDataFromGes(String ges, String river, String year) {
//        try {
//            return seleniumDS.collectionData(ges, river, year);
//        } catch (Exception ex) {
//            log.info("Ошибка при считывании данных об уровне: {}", ex.getMessage());
//            return null;
//        }
//    }
//
//
//}
//
