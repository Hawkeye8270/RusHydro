package com.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class CrawlingSchedulerServise {
    private final AutoStartCrowlingService crawlingService;
    private LocalDate currentDate;

    @Autowired
    public CrawlingSchedulerServise(AutoStartCrowlingService crawlingService, TaskScheduler taskScheduler) {
        this.crawlingService = crawlingService;
        this.taskScheduler = taskScheduler;
        // Начальная дата (можно вынести в конфигурацию)
//        this.currentDate = LocalDate.of(2025, Month.MARCH, 15);
    }

    @Value("${crawling.initial-date}")
    private String initialDate;

    @Value("${crawling.interval}")
    private long interval;

    @PostConstruct
    public void init() {
        this.currentDate = LocalDate.parse(initialDate);
    }

// --------------------------------------------------------------------------------------------------------
    private TaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledTask;

    @Autowired
    public void CrawlingSchedulerService(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    // Метод для ручного запуска периодического выполнения
    public void startScheduledCrawling(Duration interval) {
        if (scheduledTask == null || scheduledTask.isCancelled()) {
            scheduledTask = taskScheduler.scheduleAtFixedRate(
                    this::scheduleCrawling,
                    interval
            );
        }
    }

    // Метод для остановки
    public void stopScheduledCrawling() {
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
        }
    }

    // Бывший @Scheduled метод теперь вызывается вручную
    @Scheduled(fixedDelayString = "${crawling.interval}")
    public void scheduleCrawling() {
        String river = "Волга2";
        String ges = "Рыбинская";
        crawlingService.autoStartCrowling(river, ges, currentDate);

        // Увеличиваем дату на 1 месяц
        currentDate = currentDate.plusMonths(1);

        log.info("Следующий запуск краулинга запланирован на: {}", currentDate);
    }




//    @Scheduled(fixedDelayString = "${crawling.interval}") // Запуск каждые 30 min
//    public void scheduleCrawling() {
//        String river = "Волга";
//        String ges = "Рыбинская";
//
//        crawlingService.autoStartCrowling(river, ges, currentDate);
//
//        // Увеличиваем дату на 1 месяц
//        currentDate = currentDate.plusMonths(1);
//
//        log.info("Следующий запуск краулинга запланирован на: {}", currentDate);
//    }
}
