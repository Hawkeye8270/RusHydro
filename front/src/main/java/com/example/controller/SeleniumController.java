package com.example.controller;


import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// !!!!!!!!!!!!!!!! ЗАДАЧИ, КОТОРЫЕ НУЖНЫ ДЛЯ FRONTEND !!!!!!!!!!!!!

@Controller
@RequestMapping("/")
//@CrossOrigin
public class SeleniumController {
//    private static final Logger logger = LoggerFactory.getLogger(SeleniumController.class);

    @GetMapping("/")
    public ResponseEntity<Resource> home() {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(new ClassPathResource("static/html/index.html"));
    }
}
