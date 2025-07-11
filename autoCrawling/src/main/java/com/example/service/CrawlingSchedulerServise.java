package com.example.service;

import com.example.controller.CrawlerParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;


@Service
@Slf4j
@ConditionalOnProperty(name = "crawler.enabled", havingValue = "true", matchIfMissing = true)
public class CrawlingSchedulerServise {
    private final AutoStartCrowlingService crawlingService;

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public LocalDate currentDate;

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

//    @Value("${crawling.interval}")
//    private long interval;


    private CrawlerParams currentParams;

    @PostConstruct
    public void init() {
        this.currentDate = LocalDate.parse(initialDate);
        this.currentParams = new CrawlerParams("Волга", "Рыбинская", currentDate);
    }

    // ------ РАНДОМНЫЙ ИНТЕРВАЛ ЗАПУСКА АВТОСБОРЩИКА ---------------------------------------------------------------------------------------------------

    @Value("${crawling.interval:600000}") // 600000 мс = 10 минут
    private long baseInterval;

    private long nextExecutionTime = System.currentTimeMillis();

    @Scheduled(fixedRate = 10_000) // Проверяем каждые 10 секунд
    public void scheduledCrawling() {
        long now = System.currentTimeMillis();
        if (now >= nextExecutionTime) {
            executeCrawling("Волга", "Рыбинская", currentDate);
            currentDate = currentDate.plusMonths(1);

            // Случайный интервал (±2 минуты)
            long randomOffset = ThreadLocalRandom.current().nextLong(-120_000, 120_000);
            nextExecutionTime = now + baseInterval + randomOffset;
        }
    }

//    private void executeCrawling(String river, String station, LocalDate date) {
//        System.out.println("Crawling at: " + date + " with river: " + river + ", station: " + station);
//    }

    public void executeCrawling(String river, String ges, LocalDate date) {
        crawlingService.autoStartCrowling(river, ges, date);
        log.info("Краулинг выполнен: {} {} {}", river, ges, date);
    }

//-------------------------------------------------------------------------------------------------------------------------------
    /*
    // ИНТЕРВАЛ ЗАПУСКА 3 МИН +-1 МИН.
//    @Scheduled(fixedDelayString = "#{T(java.util.concurrent.ThreadLocalRandom).current().nextLong(${crawling.interval:180000} - 60000, ${crawling.interval:180000} + 60000)}")
    @Scheduled(fixedDelayString = "#{T(java.util.concurrent.ThreadLocalRandom).current().nextLong(" +
            "${crawling.interval:180000} - 120000, " +  // Минимум: интервал - 2 минуты
            "${crawling.interval:180000} + 120000" +     // Максимум: интервал + 2 минуты
            ")}")
    public void scheduledCrawling() {
        executeCrawling("Волга", "Рыбинская", currentDate);
        currentDate = currentDate.plusMonths(1);
    }

     */

    // ФИКСИРОВАННЫЙ ИНТЕРВАЛ
//    @Scheduled(fixedRateString = "${crawler.interval:60000}")
//    public void scheduledCrawling() {
//        executeCrawling("Волга", "Рыбинская", currentDate);
//        currentDate = currentDate.plusMonths(1);
//    }


}
