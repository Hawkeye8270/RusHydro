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

    @Value("${crawling.interval}")
    private long interval;


    private CrawlerParams currentParams;

    @PostConstruct
    public void init() {
        this.currentDate = LocalDate.parse(initialDate);
        this.currentParams = new CrawlerParams("Волга", "Рыбинская", currentDate);
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
}
