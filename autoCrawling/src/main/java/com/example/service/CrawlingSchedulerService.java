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
import java.util.List;
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
