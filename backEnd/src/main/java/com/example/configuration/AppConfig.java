package com.example.configuration;

import com.p6spy.engine.spy.P6DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@EnableTransactionManagement
public class AppConfig {


    @Bean
    public MappingJackson2JsonView jsonMapper() {
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setPrettyPrint(true);
        return view;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        properties.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        properties.put(Environment.HBM2DDL_AUTO, "validate");

        // Дополнительные рекомендуемые настройки для PostgreSQL
        properties.put(Environment.SHOW_SQL, "true");
        properties.put(Environment.FORMAT_SQL, "true");
        properties.put(Environment.USE_SQL_COMMENTS, "true");
        properties.put(Environment.JDBC_TIME_ZONE, "UTC");

        return properties;
    }

//    //  РАБОТАЕТ БЕЗ DOCKER
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.p6spy.engine.spy.P6SpyDriver");
//        dataSource.setJdbcUrl("jdbc:p6spy:postgresql://localhost:5432/rushydro");
        dataSource.setJdbcUrl("jdbc:p6spy:postgresql://db:5432/rushydro");
//        dataSource.setJdbcUrl("jdbc:p6spy:postgresql://my-postgres:5432/rushydro");
        dataSource.setUsername("postgres");
        dataSource.setPassword("123");
        dataSource.setMaximumPoolSize(10);
        return dataSource;
    }



//
//    private final org.springframework.core.env.Environment environment;
//    public AppConfig(org.springframework.core.env.Environment environment) {
//        this.environment = environment;
//    }
//
//    @Bean
//    public DataSource dataSource() {
//        // 1. Сначала создаем обычный DataSource
//        HikariDataSource actualDataSource = new HikariDataSource();
//        actualDataSource.setJdbcUrl("jdbc:postgresql://db:5432/rushydro");
//        actualDataSource.setDriverClassName("org.postgresql.Driver");
//        actualDataSource.setUsername("postgres");
//        actualDataSource.setPassword("123");
//        actualDataSource.setMaximumPoolSize(10);
//
//        // 2. Оборачиваем в P6Spy
//        return new P6DataSource(actualDataSource);
//    }


}
