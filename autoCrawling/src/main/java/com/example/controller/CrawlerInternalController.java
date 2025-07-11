package com.example.controller;


import com.example.service.CrawlingSchedulerServise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/crawler")
//@CrossOrigin
public class CrawlerInternalController {

    private final Optional<CrawlingSchedulerServise> crawlerService;
    private CrawlerParams currentParams;

    @Value("${crawler.default.river:Волга}")
    private String defaultRiver;

    @Value("${crawler.default.ges:Рыбинская}")
    private String defaultGes;

    // ---------------------------------------------------------------------------------------------------------

    @Value("${crawling.initial-date}")
    private String initialDate;
    private LocalDate currentDate;

    @PostConstruct
    public void init() {
        this.currentDate = LocalDate.parse(initialDate);
        this.currentParams = new CrawlerParams("Волга", "Рыбинская", currentDate);
    }
    // ---------------------------------------------------------------------------------------------------------

//    @GetMapping("/params")
//    public ResponseEntity<CrawlerParams> getCurrentParams() {
//        return ResponseEntity.ok(currentParams);
//    }

    @GetMapping("/params")
    public ResponseEntity<CrawlerParams> getCurrentParams() {
        return ResponseEntity.ok(currentParams); // Убрали ручные заголовки
    }


//    @RequestMapping(value = "/params", method = {RequestMethod.GET, RequestMethod.OPTIONS})
//    public ResponseEntity<CrawlerParams> getCurrentParams(HttpServletRequest request) {
//
//        // Для OPTIONS запроса возвращаем только заголовки
//        if (request.getMethod().equals("OPTIONS")) {
//            return ResponseEntity.ok()
//                    .header("Access-Control-Allow-Origin", "http://localhost:8080")
//                    .header("Access-Control-Allow-Methods", "GET, OPTIONS")
//                    .header("Access-Control-Allow-Headers", "content-type")
//                    .header("Access-Control-Allow-Credentials", "true")
//                    .build();
//        }
//        // Убедитесь, что метод возвращает правильные заголовки
//        // Основная логика для GET запроса
//        return ResponseEntity.ok()
//                .header("Access-Control-Allow-Origin", "http://localhost:8080")
//                .header("Access-Control-Allow-Credentials", "true")
//                .body(currentParams);
//    }

//    @GetMapping("/params")
//    public ResponseEntity<CrawlerParams> getCurrentParams() {

//        return ResponseEntity.ok()
//                .header("Access-Control-Allow-Credentials", "true")
//                .body(currentParams);
//    }


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


    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<?> options() {
        return ResponseEntity.ok().build();
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


//    private LocalDate currentDate;
//    @Value("${crawling.initial-date}")
//    private String initialDate;
//    @PostConstruct
//    public void init() {
//        this.currentDate = LocalDate.parse(initialDate);
//    }
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