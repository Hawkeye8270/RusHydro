package com.example.configuration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan("com.example")
public class WedConfig implements WebMvcConfigurer, ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
//    @Bean
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/html/**").addResourceLocations("/html/");
        registry.addResourceHandler("/html/**").addResourceLocations("classpath:/static/html/");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
//        registry.addResourceHandler("/script/**").addResourceLocations("/script/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");

    }

//    // ЭТОТ ВАРИАНТ РАБОТАЕТ
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry
//                .addResourceHandler("/js/**")
//                .addResourceLocations("classpath:/static/js/")
//                .setCacheControl(CacheControl.noCache())
//                .resourceChain(true)
//                .addResolver(new PathResourceResolver() {
//                    @Override
//                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
//                        Resource resource = super.getResource(resourcePath, location);
//                        if (resource != null) {
//                            // Возвращаем оригинальный Resource, но с UTF-8
//                            return resource;
//                        }
//                        return null;
//                    }
//                });
//    }

}
