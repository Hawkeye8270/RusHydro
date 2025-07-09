package com.example.controller;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class CrawlerParams {
    private String river;
    private String ges;
    private LocalDate date;

    // конструкторы, геттеры и сеттеры
    public CrawlerParams() {}

    public CrawlerParams(String river, String ges, LocalDate date) {
        this.river = river;
        this.ges = ges;
        this.date = date;
    }

    public String getRiver() {
        return river;
    }

    public void setRiver(String river) {
        this.river = river;
    }

    public String getGes() {
        return ges;
    }

    public void setGes(String ges) {
        this.ges = ges;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
