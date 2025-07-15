package com.example.service;

import com.example.repository.CurretnGesAndRiver;
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
public class CrawlingSchedulerService {

    private final AutoStartCrowlingService crawlingService;

    CurretnGesAndRiver curretnGesAndRiver = new CurretnGesAndRiver();
    private @Value("${crawling.initial-date}") String initialDate;
    private @Value("${crawling.interval}") long baseInterval;
    private String river = curretnGesAndRiver.getRiver();
    private String station = curretnGesAndRiver.getStation();


    private LocalDate currentDate;
    private long nextExecutionTime = System.currentTimeMillis();

    @Autowired
    public CrawlingSchedulerService(AutoStartCrowlingService crawlingService) {
        this.crawlingService = crawlingService;
    }

    @PostConstruct
    public void init() {
        try {
            this.currentDate = initialDate != null ? LocalDate.parse(initialDate) : LocalDate.now();
        } catch (Exception e) {
            log.error("Ошибка парсинга даты, используется текущая дата", e);
            this.currentDate = LocalDate.now();
        }
    }

    @Scheduled(fixedRate = 10_000) // Проверка каждые 10 секунд
    public synchronized void scheduledCrawling() {
        long now = System.currentTimeMillis();
        if (now >= nextExecutionTime) {
            executeCrawling(river, station, currentDate);
            currentDate = currentDate.plusMonths(1);

            long randomOffset = ThreadLocalRandom.current().nextLong(-120_000, 120_000);
            long nextDelay = Math.max(0, baseInterval + randomOffset);
            nextExecutionTime = now + nextDelay;
        }
    }

    public void executeCrawling(String river, String ges, LocalDate date) {
        try {
            crawlingService.autoStartCrowling(river, ges, date);
            log.info("Краулинг выполнен: {} {} {}", river, ges, date);
        } catch (Exception e) {
            log.error("Ошибка при выполнении краулинга", e);
        }
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }
}




/*

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
//            this.currentDate = initialDate != null ? LocalDate.parse(initialDate) : LocalDate.now();
        } catch (Exception e) {
            log.error("Ошибка парсинга даты, используется текущая дата", e);
            this.currentDate = LocalDate.now();
        }
    }

    @Value("${crawling.initial-date}")
    private String initialDate;

    private CrawlerParams currentParams;

//    @PostConstruct
//    public void init() {
//        this.currentDate = LocalDate.parse(initialDate);
//        this.currentParams = new CrawlerParams("Волга", "Рыбинская", currentDate);
//    }

    @PostConstruct
    public void init() {
        try {
            this.currentDate = initialDate != null ? LocalDate.parse(initialDate) : LocalDate.now();
        } catch (Exception e) {
            log.error("Ошибка парсинга даты, используется текущая дата", e);
            this.currentDate = LocalDate.now();
        }
        this.currentParams = new CrawlerParams("Волга", "Рыбинская", currentDate);
    }


    // ------ РАНДОМНЫЙ ИНТЕРВАЛ ЗАПУСКА АВТОСБОРЩИКА ---------------------------------------------------------------------------------------------------

    @Value("${crawling.interval:300000}") // 600000 мс = 10 минут
    private long baseInterval;

    private long nextExecutionTime = System.currentTimeMillis();

    @Scheduled(fixedRate = 10_000) // Проверяем каждые 10 секунд
    public void scheduledCrawling() {
        long now = System.currentTimeMillis();
        if (now >= nextExecutionTime) {
            executeCrawling("Волга", "Жигулевская", currentDate);
            currentDate = currentDate.plusMonths(1);

            // Случайный интервал (±2 минуты)
            long randomOffset = ThreadLocalRandom.current().nextLong(-120_000, 120_000);
            nextExecutionTime = now + baseInterval + randomOffset;
        }
    }

    public void executeCrawling(String river, String ges, LocalDate date) {
        crawlingService.autoStartCrowling(river, ges, date);
        log.info("Краулинг выполнен: {} {} {}", river, ges, date);
    }
}

 */
