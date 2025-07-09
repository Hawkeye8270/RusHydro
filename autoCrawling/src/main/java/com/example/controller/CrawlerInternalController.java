package com.example.controller;


import com.example.service.CrawlingSchedulerServise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/crawler")
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
public class CrawlerInternalController {

    private final CrawlingSchedulerServise crawlerSchedulerService;

    @Autowired
    public CrawlerInternalController(CrawlingSchedulerServise crawlerSchedulerService) {
        this.crawlerSchedulerService = crawlerSchedulerService;
    }


    @PostMapping("/manual-start")
    public ResponseEntity<String> manualStart(
            @RequestParam(required = false) String river,
            @RequestParam(required = false) String ges,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        // Если параметры не переданы, используем значения по умолчанию
        if (river == null) river = "Волга";
        if (ges == null) ges = "Рыбинская";
        if (date == null) date = LocalDate.now();

        crawlerSchedulerService.scheduleCrawling();

        return ResponseEntity.ok("Ручной запуск краулинга выполнен для: "
                + river + ", " + ges + ", " + date);
    }



        @Autowired
        private CrawlerScheduler crawlerScheduler;
        @PostMapping("/enable")
        public ResponseEntity<String> enableCrawler(@RequestParam boolean enable) {
            crawlerScheduler.enableCrawler(enable);
            return ResponseEntity.ok("Состояние автосборщика: " + (enable ? "активирован" : "деактивирован"));
        }



}