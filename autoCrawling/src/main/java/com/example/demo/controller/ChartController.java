package com.example.demo.controller;

import com.example.demo.entity.Data;
import com.example.demo.repository.DataDBRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@Slf4j
public class ChartController {
    private final DataDBRepository dataDBRepository;
    private final ObjectMapper objectMapper;

    public ChartController(DataDBRepository dataDBRepository, ObjectMapper objectMapper) {
        this.dataDBRepository = dataDBRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping(value = "/chart", produces = "text/html;charset=UTF-8")
    public ResponseEntity<byte[]> testStaticAccess() {
        try {
            Resource resource = new ClassPathResource("static/html/chart.html");
            byte[] fileBytes = Files.readAllBytes(Paths.get(resource.getURI()));

//            // Проверка содержимого (для отладки)
//            String contentCheck = new String(fileBytes, StandardCharsets.UTF_8);
//            System.out.println("Первые 100 символов файла: " + contentCheck.substring(0, Math.min(100, contentCheck.length())));

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .header("Content-Type", "text/html;charset=UTF-8")
                    .body(fileBytes);
        } catch (Exception e) {
            // Возвращаем ошибку тоже в виде байтов с UTF-8
            String errorMessage = "Ошибка доступа к файлу: " + e.getMessage();
            return ResponseEntity.status(500)
                    .contentType(MediaType.TEXT_HTML)
                    .body(errorMessage.getBytes(StandardCharsets.UTF_8));
        }
    }

    // РАБОТАЕТ С РУКАМИ ЗАДАННЫМИ ДАННЫМИ
//    @GetMapping("/chart-data")
//    @ResponseBody
//    public ResponseEntity<?> getChartData() {
//        // Жёстко заданные параметры
//        final String river = "Волга";
//        final String ges = "Рыбинская";
//        final String dateStartStr = "2025-04-20";
//        final String dateFinishStr = "2025-05-10";
//
//        try {
//            // Конвертируем строки в java.util.Date
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            sdf.setLenient(false);
//            Date dateStart = sdf.parse(dateStartStr);
//            Date dateFinish = sdf.parse(dateFinishStr);
//
//            // Получаем данные из БД
//            List<Data> dataList = dataDBRepository.findByRiverAndGesAndDateBetween(
//                    river,
//                    ges,
//                    dateStart,
//                    dateFinish
//            );
//
//            if (dataList == null || dataList.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(Map.of(
//                                "error", "Данные не найдены",
//                                "parameters", Map.of(
//                                        "river", river,
//                                        "ges", ges,
//                                        "dateStart", dateStartStr,
//                                        "dateFinish", dateFinishStr
//                                )
//                        ));
//            }
//
//            // Создаем DTO с java.util.Date (без конвертации в LocalDate)
//            List<ChartDataDto> chartData = dataList.stream()
//                    .map(data -> new ChartDataDto(data.getDate(), data.getLevel()))
//                    .collect(Collectors.toList());
//
//            return ResponseEntity.ok()
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(chartData);
//
//        } catch (ParseException e) {
//            return ResponseEntity.badRequest()
//                    .body(Map.of("error", "Неверный формат даты. Используйте YYYY-MM-DD"));
//        } catch (Exception e) {
//            log.error("Ошибка при получении данных для {}/{}, период {} - {}",
//                    river, ges, dateStartStr, dateFinishStr, e);
//            return ResponseEntity.internalServerError()
//                    .body(Map.of("error", "Внутренняя ошибка сервера"));
//        }
//    }



//    @PostMapping("/api/requestToBD")
//    public ResponseEntity<String> handlePostRequest(@RequestBody RequestDataToDB data, HttpServletRequest request) throws ParseException {
//        log.info("=== POST запрос получен ===");
//        log.info("Река: {}", data.getRiver());
//        log.info("ГЭС: {}", data.getGes());
//        log.info("Дата начала: {}", data.getDateStart());
//        log.info("Дата окончания: {}", data.getDateFinish());
//
//        // Сохраняем параметры в сессии
//        request.getSession().setAttribute("river", data.getRiver());
//        request.getSession().setAttribute("ges", data.getGes());
//        request.getSession().setAttribute("dateStart", data.getDateStart());
//        request.getSession().setAttribute("dateFinish", data.getDateFinish());
//
//        SeleniumRequestDataService.requestDataFromDB(data.getRiver(), data.getGes(), data.getDateStart(), data.getDateFinish());
//
//        return ResponseEntity.ok("Данные получены");
//    }

    @GetMapping("/chart-data")
    @ResponseBody
    public ResponseEntity<?> getChartData(HttpServletRequest request) {
        // Получаем параметры из сессии
        String river = (String) request.getSession().getAttribute("river");
        String ges = (String) request.getSession().getAttribute("ges");
        String dateStartStr = (String) request.getSession().getAttribute("dateStart");
        String dateFinishStr = (String) request.getSession().getAttribute("dateFinish");

        try {
            // Конвертируем строки в java.util.Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            Date dateStart = sdf.parse(dateStartStr);
            Date dateFinish = sdf.parse(dateFinishStr);

            // Получаем данные из БД
            List<Data> dataList = dataDBRepository.findByRiverAndGesAndDateBetween(
                    river,
                    ges,
                    dateStart,
                    dateFinish
            );

            if (dataList == null || dataList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "Данные не найдены",
                                "parameters", Map.of(
                                        "river", river,
                                        "ges", ges,
                                        "dateStart", dateStartStr,
                                        "dateFinish", dateFinishStr
                                )
                        ));
            }

            // Создаем DTO с java.util.Date (без конвертации в LocalDate)
            List<ChartDataDto> chartData = dataList.stream()
                    .map(data -> new ChartDataDto(data.getDate(), data.getLevel()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(chartData);

        } catch (ParseException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Неверный формат даты. Используйте YYYY-MM-DD"));
        } catch (Exception e) {
            log.error("Ошибка при получении данных для {}/{}, период {} - {}",
                    river, ges, dateStartStr, dateFinishStr, e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Внутренняя ошибка сервера"));
        }
    }



}
