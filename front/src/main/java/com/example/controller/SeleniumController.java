package com.example.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class SeleniumController {

    @GetMapping("/")
    public ResponseEntity<Resource> home() {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(new ClassPathResource("static/html/index.html"));
    }
}
