package com.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CrawlerScheduler {
    private volatile boolean isCrawlerEnabled = false;

//    @Autowired
//    private CrawlerService crawlerService;

    private CrawlingSchedulerServise crawlingSchedulerServise;

    // Метод для внешнего вызова
    public void enableCrawler(boolean enable) {
        this.isCrawlerEnabled = enable;
        if (enable) {
            log.info("Автосборщик активирован");
        } else {
            log.info("Автосборщик деактивирован");
        }
    }

    @Scheduled(fixedRate = 180000) // Проверка каждые 3 минуты
    public void checkAndRunCrawler() {
        if (isCrawlerEnabled) {
            crawlingSchedulerServise.scheduleCrawling();
        }
    }
}
