package com.example.repository;
//
//import com.example.entity.Data;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.Date;
//import java.util.List;
//
//@Repository         // ???????????????????
//public interface DataDBRepository extends JpaRepository<Data, Long> {
////    List<Data> findAllByOrderByDateAsc();
//
//    // Дополнительные методы при необходимости
//    List<Data> findByRiver(String river);
//
//    List<Data> findByGes(String ges);
//
//    @Query("SELECT DISTINCT d.river FROM Data d")
//    List<String> findDistinctRiver();
//
//    @Query("SELECT DISTINCT d.ges FROM Data d")
//    List<String> findDistinctGes();
//
//    List<Data> findByRiverOrderByDateAsc(String river);
//
//    List<Data> findByGesOrderByDateAsc(String ges);
//
//    List<Data> findAllByOrderByDateAsc();
//
//    // Вариант 1: Используя соглашение об именовании Spring Data JPA
////     List<Data> findByRiverAndGesAndDateBetween(String river, String ges, Date startDate, Date endDate);
//     List<Data> findByRiverAndGesAndDateBetween(String river, String ges, Date dateStart, Date dateFinish);
//
//    // ИЛИ Вариант 2: С явным JPQL запросом (более гибкий вариант)
////    @Query("SELECT d FROM Data d WHERE d.river = :river AND d.ges = :ges AND d.date BETWEEN :startDate AND :endDate ORDER BY d.date ASC")
////    List<Data> findByRiverAndGesAndDateBetween(
////            @Param("river") String river,
////            @Param("ges") String ges,
//////            @Param("startDate") LocalDate startDate,
//////            @Param("endDate") LocalDate endDate);
////            @Param("startDate") Date startDate,
////            @Param("endDate") Date endDate);
////}
//
//
//}


