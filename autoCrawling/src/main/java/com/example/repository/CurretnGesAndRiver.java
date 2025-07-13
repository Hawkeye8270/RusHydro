package com.example.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class CurretnGesAndRiver {

//    //    private String initialDate = "2016-06-01" ;
//    private @Value("${crawling.initial-date}") String initialDate;
    private String river = "Волга";
    private String station = "Жигулевская";
//    //    private long baseInterval = 300000;
//    private @Value("${crawling.interval}") long baseInterval;


//    public String getInitialDate() {
//        return initialDate;
//    }
//
//    public void setInitialDate(String initialDate) {
//        this.initialDate = initialDate;
//    }

    public String getRiver() {
        return river;
    }

    public void setRiver(String river) {
        this.river = river;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

//    public long getBaseInterval() {
//        return baseInterval;
//    }
//
//    public void setBaseInterval(long baseInterval) {
//        this.baseInterval = baseInterval;
//    }
}
