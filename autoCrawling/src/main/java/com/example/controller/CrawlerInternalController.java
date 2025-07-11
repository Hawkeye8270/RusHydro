package com.example.controller;


import com.example.service.CrawlingSchedulerServise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@RestController
@Slf4j
@RequestMapping("/api/crawler")
public class CrawlerInternalController {

//    private CrawlerParams currentParams;

    // ---------------------------------------------------------------------------------------------------------

    @Value("${crawling.initial-date}")
    private String initialDate;
    private LocalDate currentDate;

//    @PostConstruct
//    public void init() {
//        this.currentDate = LocalDate.parse(initialDate);
//        this.currentParams = new CrawlerParams("Волга", "Рыбинская", currentDate);
//    }

    // ---------------------------------------------------------------------------------------------------------

    private final CrawlingSchedulerServise crawlerService;
    private CrawlerParams currentParams;

    @Autowired
    public CrawlerInternalController(CrawlingSchedulerServise crawlerService,
                                     @Value("${crawling.initial-date}") String initialDate) {
        this.crawlerService = crawlerService;
        this.currentDate = LocalDate.parse(initialDate);
        this.currentParams = new CrawlerParams("Волга", "Рыбинская", currentDate);
    }

    @GetMapping("/params")
    public ResponseEntity<CrawlerParams> getCurrentParams() {
        // Берем currentDate из сервиса
        currentParams.setDate(crawlerService.getCurrentDate());
        return ResponseEntity.ok(currentParams);
    }


    // ---------------------------------------------------------------------------------------------------------



//    @GetMapping("/params")
//    public ResponseEntity<CrawlerParams> getCurrentParams() {
//        return ResponseEntity.ok(currentParams); // Убрали ручные заголовки
//    }


}