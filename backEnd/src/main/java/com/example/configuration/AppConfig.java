package com.example.configuration;


import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.cfg.Environment;
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

//    //  РАБОТАЕТ БЕЗ DOCKER
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.p6spy.engine.spy.P6SpyDriver");
//        dataSource.setJdbcUrl("jdbc:p6spy:postgresql://localhost:5432/rushydro");
        dataSource.setJdbcUrl("jdbc:p6spy:postgresql://db:5432/rushydro");
        dataSource.setUsername("postgres");
        dataSource.setPassword("123");
        dataSource.setMaximumPoolSize(10);
        return dataSource;
    }
}
