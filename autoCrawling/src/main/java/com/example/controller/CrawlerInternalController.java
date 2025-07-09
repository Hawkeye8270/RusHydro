package com.example.controller;


import com.example.service.CrawlingSchedulerServise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/crawler")
//@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
@CrossOrigin
public class CrawlerInternalController {

    private final Optional<CrawlingSchedulerServise> crawlerService;
    private CrawlerParams currentParams;

    @Value("${crawler.default.river:Волга}")
    private String defaultRiver;

    @Value("${crawler.default.ges:Рыбинская}")
    private String defaultGes;


    @Autowired
    public CrawlerInternalController(
            Optional<CrawlingSchedulerServise> crawlerService,
            @Value("${crawler.default.river:Волга}") String defaultRiver,
            @Value("${crawler.default.ges:Рыбинская}") String defaultGes) {
        this.crawlerService = crawlerService;
        this.currentParams = new CrawlerParams(defaultRiver, defaultGes, LocalDate.now());
    }

//    @Autowired
//    public CrawlerInternalController(Optional<CrawlingSchedulerServise> crawlerService) {
//        this.crawlerService = crawlerService;
//        this.currentParams = new CrawlerParams(defaultRiver, defaultGes, LocalDate.now());
//    }

    @GetMapping("/params")
    public ResponseEntity<CrawlerParams> getCurrentParams() {
        return ResponseEntity.ok(currentParams);
    }

    @PostMapping("/params")
    public ResponseEntity<String> setParams(@RequestBody CrawlerParams params) {
        this.currentParams = params;
        return ResponseEntity.ok("Параметры успешно обновлены");
    }

    @PostMapping("/start")
    public ResponseEntity<String> startCrawling() {
        if (crawlerService.isEmpty()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Сервис краулера недоступен (возможно, отключен в настройках)");
        }

        crawlerService.get().executeCrawling(
                currentParams.getRiver(),
                currentParams.getGes(),
                currentParams.getDate()
        );

        return ResponseEntity.ok("Краулинг запущен с параметрами: " +
                currentParams.getRiver() + ", " +
                currentParams.getGes() + ", " +
                currentParams.getDate());
    }








//    private final CrawlingSchedulerServise crawlingSchedulerServise;


    private LocalDate currentDate;
    @Value("${crawling.initial-date}")
    private String initialDate;
    @PostConstruct
    public void init() {
        this.currentDate = LocalDate.parse(initialDate);
    }
//    private CrawlerParams currentParams = new CrawlerParams("Волга", "Рыбинская", currentDate);


//    public CrawlerInternalController(CrawlingSchedulerServise crawlingSchedulerServise) {
//        this.crawlingSchedulerServise = crawlingSchedulerServise;
//    }


//    // Получить текущие параметры
//    @GetMapping("/params")
//    public ResponseEntity<CrawlerParams> getCurrentParams() {
//        return ResponseEntity.ok(currentParams);
//    }
//
//    // Установить новые параметры
//    @PostMapping("/params")
//    public ResponseEntity<String> setParams(@RequestBody CrawlerParams params) {
//        this.currentParams = params;
//        return ResponseEntity.ok("Параметры успешно обновлены");
//    }
//
//    // Запуск краулинга с текущими параметрами
//    @PostMapping("/start")
//    public ResponseEntity<String> startCrawling() {
//        crawlingSchedulerServise.scheduleCrawling(
//                currentParams.getRiver(),
//                currentParams.getGes(),
//                currentParams.getDate()
//        );
//        return ResponseEntity.ok("Краулинг запущен с параметрами: " +
//                currentParams.getRiver() + ", " +
//                currentParams.getGes() + ", " +
//                currentParams.getDate());
//    }
//------------------------------------------------------------------------------------------------------------------

//    private final CrawlingSchedulerServise crawlerSchedulerService;
//
//    @Autowired
//    public CrawlerInternalController(CrawlingSchedulerServise crawlerSchedulerService) {
//        this.crawlerSchedulerService = crawlerSchedulerService;
//    }
//
//
//    @PostMapping("/manual-start")
//    public ResponseEntity<String> manualStart(
//            @RequestParam(required = false) String river,
//            @RequestParam(required = false) String ges,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//
//        // Если параметры не переданы, используем значения по умолчанию
//        if (river == null) river = "Волга";
//        if (ges == null) ges = "Рыбинская";
//        if (date == null) date = LocalDate.now();
//
//        crawlerSchedulerService.scheduleCrawling();
//
//        return ResponseEntity.ok("Ручной запуск краулинга выполнен для: "
//                + river + ", " + ges + ", " + date);
//    }


}