package com.example.configuration;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:8080") // или "*" для разработки
                .allowedOrigins(
                        "http://localhost:8080",
//                        "http://localhost:8081",
                        "http://localhost:8082"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // добавили OPTIONS
                .allowedHeaders("*") // разрешаем все заголовки
                .exposedHeaders("Authorization", "Content-Disposition") // какие заголовки показывать клиенту
                .allowCredentials(true)
                .maxAge(3600); // кеширование CORS-правил на 1 час
    }


//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:8080") // Разрешить запросы с 8080
//                .allowedMethods("GET", "POST", "PUT", "DELETE")
//                .allowCredentials(true);
//    }
}
