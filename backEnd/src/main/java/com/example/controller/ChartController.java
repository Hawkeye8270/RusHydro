package com.example.controller;

import com.example.entity.Data;
import com.example.repository.DataDBRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@Slf4j
@CrossOrigin
public class ChartController {
    private final DataDBRepository dataDBRepository;
    private final ObjectMapper objectMapper;

    public ChartController(DataDBRepository dataDBRepository, ObjectMapper objectMapper) {
        this.dataDBRepository = dataDBRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping(value = "/chart", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<byte[]> testStaticAccess() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/static/html/chart.html");
            if (inputStream == null) {
                throw new FileNotFoundException("Файл не найден по пути /static/html/chart.html");
            }

            byte[] fileBytes = inputStream.readAllBytes();
            String contentCheck = new String(fileBytes, StandardCharsets.UTF_8);
            System.out.println("Первые 100 символов файла: " +
                    contentCheck.substring(0, Math.min(100, contentCheck.length())));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE + ";charset=UTF-8")
                    .body(fileBytes);
        } catch (Exception e) {
            String errorMessage = "Ошибка доступа к файлу: " + e.getMessage();
            log.error(errorMessage, e);
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(errorMessage.getBytes(StandardCharsets.UTF_8));
        }
    }

    @GetMapping("/chart-data")
    @ResponseBody
    public ResponseEntity<?> getChartData(
            @RequestParam String river,
            @RequestParam String ges,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateStart,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFinish) {

        log.info("Request for {} - {}, from {} to {}", river, ges, dateStart, dateFinish);

        try {
            List<Data> dataList = dataDBRepository.findByRiverAndGesAndDateBetween(
                    river, ges, dateStart, dateFinish);

            if (dataList == null || dataList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "Данные не найдены",
                                "parameters", Map.of(
                                        "river", river,
                                        "ges", ges,
                                        "dateStart", dateStart,
                                        "dateFinish", dateFinish
                                )
                        ));
            }

            List<ChartDataDto> chartData = dataList.stream()
                    .map(data -> new ChartDataDto(data.getDate(), data.getLevel()))
                    .collect(Collectors.toList());


            return ResponseEntity.ok()
                    .header("Access-Control-Allow-Origin", "http://localhost:8080")
                    .header("Access-Control-Allow-Credentials", "true")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(chartData);

        } catch (Exception e) {
            log.error("Ошибка при обработке запроса", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Внутренняя ошибка сервера"));
        }
    }

//
//
//// В МОНОЛИТЕ РАБОТАЕТ ЭТОТ КОД!!!!!!!
//    @GetMapping("/chart-data")
////    @CrossOrigin     // (origins = "http://localhost:8080") // Укажите адрес фронтенда  ЧТО БЫ ГЛОБАЛЬНАЯ НАСТРОЙКА РАБОТАЛА??????
//    @ResponseBody
//    public ResponseEntity<?> getChartData(HttpServletRequest request) {
//
//        // Логирование входящего запроса
//        log.info("Incoming request from: {}:{}", request.getRemoteAddr(), request.getRemotePort());
//
////        // Получаем параметры из сессии при МОНОЛИТЕ!!!!
////        String river = (String) request.getSession().getAttribute("river");
////        String ges = (String) request.getSession().getAttribute("ges");
////        String dateStartStr = (String) request.getSession().getAttribute("dateStart");
////        String dateFinishStr = (String) request.getSession().getAttribute("dateFinish");
//
//        // Получаем параметры из сессии
//        String river = request.getParameter("river");
//        String ges = request.getParameter("ges");
//        String dateStartStr = request.getParameter("dateStart");
//        String dateFinishStr = request.getParameter("dateFinish");
//
//        // Логирование параметров
//        log.info("Request params - river: {}, ges: {}, dates: {} to {}",
//                river, ges, dateStartStr, dateFinishStr);
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
////            return ResponseEntity.ok()
////                    .contentType(MediaType.APPLICATION_JSON)
////                    .body(chartData);
//
//            return ResponseEntity.ok()
//                    .header("X-Served-By", "8080") // Явное указание порта в заголовке
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


}
