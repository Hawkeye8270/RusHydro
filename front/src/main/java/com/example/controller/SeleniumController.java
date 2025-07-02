package com.example.controller;

//import com.example.service.SeleniumRequestDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;


// !!!!!!!!!!!!!!!! ЗАДАЧИ, КОТОРЫЕ НУЖНЫ ДЛЯ FRONTEND !!!!!!!!!!!!!


//@RestController
@Controller
@RequestMapping("/")                                    // МОЖЕТ МЕШАТЬ
public class SeleniumController {
    private static final Logger logger = LoggerFactory.getLogger(SeleniumController.class);

    @GetMapping("/")
    public ResponseEntity<Resource> home() {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(new ClassPathResource("static/html/index.html"));
    }
//
//    @PostMapping("/api/request")
//    public ResponseEntity<String> handlePostRequest(@RequestBody RequestData data) {
//        logger.info("=== POST запрос получен ===");
//        logger.info("Река: {}", data.getRiver());
//        logger.info("ГЭС: {}", data.getGes());
//        logger.info("Дата: {}", data.getDate());
//
//        String year = data.getRequestYear(data.getDate());
//
//        String monthNumber = data.getRequestMonth(data.getDate());  // "05"
//        String monthName = getMonthName(monthNumber);               // "Май"
//
//        String dayTemp = data.getRequestDay(data.getDate());        // "05"
//        String day = String.valueOf(Integer.parseInt(dayTemp));     // "5"
//
////        System.out.println("Год - " + year);
////        System.out.println("Месяц - " + monthName);
////        System.out.println("день - " + day);
//
//        SeleniumRequestDataService.startCrowling(data.getRiver(), data.getGes(), year, monthName, day);
//
//        return ResponseEntity.ok("Данные получены");
//    }
//
//    private String getMonthName(String monthNumber) {
//        String[] months = {
//                "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
//                "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
//        };
//        int month = Integer.parseInt(monthNumber);
//        return months[month - 1];
//    }
//
//    @PostMapping("/api/requestToBD")
//    public ResponseEntity<Map<String, String>> handlePostRequest(
//            @RequestBody RequestDataToDB data,
//            HttpServletRequest request
//    ) {
//        // Сохраняем параметры в сессии (если нужно)
//        request.getSession().setAttribute("river", data.getRiver());
//        request.getSession().setAttribute("ges", data.getGes());
//        request.getSession().setAttribute("dateStart", data.getDateStart());
//        request.getSession().setAttribute("dateFinish", data.getDateFinish());
//
//        // Формируем URL для графика
//        String chartUrl = String.format(
////                "/chart?river=%s&ges=%s&dateStart=%s&dateFinish=%s",
//                "http://localhost:8081/chart?river=%s&ges=%s&dateStart=%s&dateFinish=%s",
//                URLEncoder.encode(data.getRiver(), StandardCharsets.UTF_8),
//                URLEncoder.encode(data.getGes(), StandardCharsets.UTF_8),
//                URLEncoder.encode(data.getDateStart(), StandardCharsets.UTF_8),
//                URLEncoder.encode(data.getDateFinish(), StandardCharsets.UTF_8)
//        );
//
//        // Возвращаем JSON с URL для перехода
//        return ResponseEntity.ok(Map.of("redirectUrl", chartUrl));
//    }


}
