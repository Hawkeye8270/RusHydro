package com.example.repository;

import com.example.entity.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import org.hibernate.cfg.Configuration;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

// !!!!!!!!!!!  МЕТОДЫ ДЛЯ РАБОТЫ С БД !!!!!!!!!!!!!!!!!!

@Repository
@Slf4j
public class DataRepository {

    public static void createNewData(String river, String ges, Map<Date, Float> mapOfDate) {

        Map<Date, Float> map = mapOfDate;

        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            int savedCount = 0;

            NumberFormat formatter = DecimalFormat.getInstance(Locale.US);
            formatter.setMaximumFractionDigits(2);
            formatter.setMinimumFractionDigits(2);

            for (Map.Entry<Date, Float> entry : map.entrySet()) {

                boolean exists = checkIfExists(session, river, ges, entry.getKey());

                if (!exists) {
                    Data data = new Data();
                    data.setRiver(river);
                    data.setGes(ges);
                    data.setDate(entry.getKey());
                    data.setLevel(entry.getValue());

                    session.save(data);
                    savedCount++;
                }
            }

            session.getTransaction().commit();

            System.out.println("Данные успешно сохранены в количестве " + savedCount + " новый из " + map.size() + " шт.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sessionFactory.close();
        }
    }

    private static boolean checkIfExists(Session session, String river, String ges, Date date) {
        try {

            String hql = "SELECT COUNT(d) FROM Data d WHERE " +
                    "d.river = :river AND " +
                    "d.ges = :ges AND " +
                    "d.date = :date";

            // Выполняем запрос и получаем результат
            Long count = session.createQuery(hql, Long.class)
                    .setParameter("river", river)
                    .setParameter("ges", ges)
                    .setParameter("date", date)
                    .uniqueResult();

            // Если count > 0, значит запись уже существует
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false; // В случае ошибки считаем, что записи нет
        }
    }
}
