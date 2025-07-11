package com.example.service;

import com.example.controller.CrawlerParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.concurrent.ScheduledFuture;

@Service
@Slf4j
@ConditionalOnProperty(name = "crawler.enabled", havingValue = "true", matchIfMissing = true)
public class CrawlingSchedulerServise {
    private final AutoStartCrowlingService crawlingService;

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    private LocalDate currentDate;

//    @Autowired
//    public CrawlingSchedulerServise(AutoStartCrowlingService crawlingService, TaskScheduler taskScheduler) {
//        this.crawlingService = crawlingService;
//        this.taskScheduler = taskScheduler;
//        // Начальная дата (можно вынести в конфигурацию)
////        this.currentDate = LocalDate.of(2025, Month.MARCH, 15);
//    }


    @Autowired
    public CrawlingSchedulerServise(
            AutoStartCrowlingService crawlingService,
            @Value("${crawling.initial-date}") String initialDate) {
        this.crawlingService = crawlingService;
        try {
            this.currentDate = initialDate != null ? LocalDate.parse(initialDate) : LocalDate.now();
        } catch (Exception e) {
            log.error("Ошибка парсинга даты, используется текущая дата", e);
            this.currentDate = LocalDate.now();
        }
    }

    @Value("${crawling.initial-date}")
    private String initialDate;

    @Value("${crawling.interval}")
    private long interval;

//    @PostConstruct
//    public void init() {
//        this.currentDate = LocalDate.parse(initialDate);
//    }

    private CrawlerParams currentParams;

    @PostConstruct
    public void init() {
        this.currentDate = LocalDate.parse(initialDate);
        this.currentParams = new CrawlerParams("Волга", "Рыбинская", currentDate);
    }

// --------------------------------------------------------------------------------------------------------
    private TaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledTask;

    @Autowired
    public void CrawlingSchedulerService(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }


    // Метод для @Scheduled (без параметров)
//    @Scheduled(fixedDelayString = "${crawling.interval}")
//    public void scheduledCrawling() {
//        // Используем значения по умолчанию или текущие из класса
//        scheduleCrawling("Волга", "Рыбинская", currentDate);
//        currentDate = currentDate.plusMonths(1);
//    }

    // Метод для ручного вызова (с параметрами)
    public void scheduleCrawling(String river, String ges, LocalDate date) {
        crawlingService.autoStartCrowling(river, ges, date);
        log.info("Краулинг выполнен для: {}, {}, {}", river, ges, date);
    }

    @Scheduled(fixedRateString = "${crawler.interval:300000}")
    public void scheduledCrawling() {
        executeCrawling("Волга", "Рыбинская", currentDate);
        currentDate = currentDate.plusMonths(1);
    }

    public void executeCrawling(String river, String ges, LocalDate date) {
        crawlingService.autoStartCrowling(river, ges, date);
        log.info("Краулинг выполнен: {} {} {}", river, ges, date);
    }

//
//    @Scheduled(fixedDelayString = "${crawling.interval}")
//    public void scheduleCrawling() {
//        // Теперь метод без параметров, так как они будут переданы из контроллера
//    }
//
//    @Scheduled(fixedDelayString = "${crawling.interval}")
//    public void scheduleCrawling(String river, String ges, LocalDate date) {
//        crawlingService.autoStartCrowling(river, ges, date);
//        log.info("Следующий запуск краулинга запланирован для: {}, {}, {}", river, ges, date);
//    }

    // ДО 09.07
//    // Бывший @Scheduled метод теперь вызывается вручную
//    @Scheduled(fixedDelayString = "${crawling.interval}")
//    public void scheduleCrawling() {
//        String river = "Волга";
//        String ges = "Рыбинская";
//        crawlingService.autoStartCrowling(river, ges, currentDate);
//
//        // Увеличиваем дату на 1 месяц
//        currentDate = currentDate.plusMonths(1);
//
//        log.info("Следующий запуск краулинга запланирован на: {}", currentDate);
//    }

}
