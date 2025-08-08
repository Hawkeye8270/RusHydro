package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

// !!!!!!!!!!!!!!!! ЗАДАЧИ, КОТОРЫЕ НУЖНЫ ДЛЯ FRONTEND !!!!!!!!!!!!!

@Controller
@RequestMapping("/")
@CrossOrigin
public class SeleniumController {

    private static final Logger logger = LoggerFactory.getLogger(SeleniumController.class);

    @PostMapping("/api/requestToBD")
    public ResponseEntity<Map<String, String>> handlePostRequest(
            @RequestBody RequestDataToDB data,
            HttpServletRequest request
    ) {
        // Сохраняем параметры в сессии (если нужно)
        request.getSession().setAttribute("river", data.getRiver());
        request.getSession().setAttribute("ges", data.getGes());
        request.getSession().setAttribute("dateStart", data.getDateStart());
        request.getSession().setAttribute("dateFinish", data.getDateFinish());

        // Формируем URL для графика
        String chartUrl = String.format(
                "/chart?river=%s&ges=%s&dateStart=%s&dateFinish=%s",
                URLEncoder.encode(data.getRiver(), StandardCharsets.UTF_8),
                URLEncoder.encode(data.getGes(), StandardCharsets.UTF_8),
                URLEncoder.encode(data.getDateStart(), StandardCharsets.UTF_8),
                URLEncoder.encode(data.getDateFinish(), StandardCharsets.UTF_8)
        );

        // Возвращаем JSON с URL для перехода
        return ResponseEntity.ok(Map.of("redirectUrl", chartUrl));
    }
}
