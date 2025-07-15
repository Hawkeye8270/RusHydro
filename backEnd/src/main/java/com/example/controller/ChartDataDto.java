package com.example.controller;

import java.util.Date;


public class ChartDataDto {
    private Date date; // timestamp
    private float level;

    public ChartDataDto(Date date, float level) {
        this.date = date;
        this.level = level;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getLevel() {
        return level;
    }

    public void setLevel(float level) {
        this.level = level;
    }

}