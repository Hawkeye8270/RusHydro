package com.example.repository;

import com.example.entity.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DataDBRepository extends JpaRepository<Data, Long> {


    // Дополнительные методы при необходимости
    List<Data> findByRiver(String river);

    List<Data> findByGes(String ges);

    @Query("SELECT DISTINCT d.river FROM Data d")
    List<String> findDistinctRiver();

    @Query("SELECT DISTINCT d.ges FROM Data d")
    List<String> findDistinctGes();

    List<Data> findByRiverOrderByDateAsc(String river);

    List<Data> findByGesOrderByDateAsc(String ges);

    List<Data> findAllByOrderByDateAsc();

    List<Data> findByRiverAndGesAndDateBetween(String river, String ges, Date dateStart, Date dateFinish);
}


