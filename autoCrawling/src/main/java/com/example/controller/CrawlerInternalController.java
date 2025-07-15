package com.example.controller;


import com.example.repository.CurretnGesAndRiver;
import com.example.service.AutoStartCrowlingService;
import com.example.service.CrawlingSchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@Slf4j
@RequestMapping("/api/crawler")
public class CrawlerInternalController {
    private static final Logger logger = LoggerFactory.getLogger(CrawlerInternalController.class);
    private LocalDate currentDate;
    private final CrawlingSchedulerService crawlerService;
    private CrawlerParams currentParams;

    // -------------------------------------------------------------------------------------------------
    CurretnGesAndRiver curretnGesAndRiver = new CurretnGesAndRiver();
    private String river = curretnGesAndRiver.getRiver();
    private String ges = curretnGesAndRiver.getStation();
// -------------------------------------------------------------------------------------------------


    @Autowired
    public CrawlerInternalController(CrawlingSchedulerService crawlerService,
                                     @Value("${crawling.initial-date}") String initialDate) {
        this.crawlerService = crawlerService;
        this.currentDate = LocalDate.parse(initialDate);
        this.currentParams = new CrawlerParams(river, ges, currentDate);
    }

    @GetMapping("/params")
    public ResponseEntity<CrawlerParams> getCurrentParams() {
        currentParams.setDate(crawlerService.getCurrentDate());
        return ResponseEntity.ok(currentParams);
    }

    @PostMapping("/request")
    public ResponseEntity<String> handlePostRequest(@RequestBody RequestData data) {
        logger.info("=== POST запрос получен ===");
        logger.info("Река: {}", data.getRiver());
        logger.info("ГЭС: {}", data.getGes());
        logger.info("Дата: {}", data.getDate());

        String year = data.getRequestYear(data.getDate());

        String monthNumber = data.getRequestMonth(data.getDate());  // "05"
        String monthName = getMonthName(monthNumber);               // "Май"

        String dayTemp = data.getRequestDay(data.getDate());        // "05"
        String day = String.valueOf(Integer.parseInt(dayTemp));     // "5"

        AutoStartCrowlingService.startCrowling(data.getRiver(), data.getGes(), year, monthName, day);

        return ResponseEntity.ok("Данные получены");
    }

    private String getMonthName(String monthNumber) {
        String[] months = {
                "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
                "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
        };
        int month = Integer.parseInt(monthNumber);
        return months[month - 1];
    }
}