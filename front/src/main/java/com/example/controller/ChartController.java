package com.example.controller;
//
//import com.example.entity.Data;
//import com.example.repository.DataDBRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//

//@RestController
//@Slf4j
//public class ChartController {
//    private final DataDBRepository dataDBRepository;
//    private final ObjectMapper objectMapper;
//
//    public ChartController(DataDBRepository dataDBRepository, ObjectMapper objectMapper) {
//        this.dataDBRepository = dataDBRepository;
//        this.objectMapper = objectMapper;
//    }
//
//    @GetMapping(value = "/chart", produces = "text/html;charset=UTF-8")
//    public ResponseEntity<byte[]> testStaticAccess() {
//        try {
//            Resource resource = new ClassPathResource("static/html/chart.html");
//            byte[] fileBytes = Files.readAllBytes(Paths.get(resource.getURI()));
//
////            // Проверка содержимого (для отладки)
////            String contentCheck = new String(fileBytes, StandardCharsets.UTF_8);
////            System.out.println("Первые 100 символов файла: " + contentCheck.substring(0, Math.min(100, contentCheck.length())));
//
//            return ResponseEntity.ok()
//                    .contentType(MediaType.TEXT_HTML)
//                    .header("Content-Type", "text/html;charset=UTF-8")
//                    .body(fileBytes);
//        } catch (Exception e) {
//            // Возвращаем ошибку тоже в виде байтов с UTF-8
//            String errorMessage = "Ошибка доступа к файлу: " + e.getMessage();
//            return ResponseEntity.status(500)
//                    .contentType(MediaType.TEXT_HTML)
//                    .body(errorMessage.getBytes(StandardCharsets.UTF_8));
//        }
//    }
//
//    @GetMapping("/chart-data")
//    @ResponseBody
//    public ResponseEntity<?> getChartData(HttpServletRequest request) {
//        // Получаем параметры из сессии
//        String river = (String) request.getSession().getAttribute("river");
//        String ges = (String) request.getSession().getAttribute("ges");
//        String dateStartStr = (String) request.getSession().getAttribute("dateStart");
//        String dateFinishStr = (String) request.getSession().getAttribute("dateFinish");
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
//
//
//
//}
