package com.familyfirstsoftware.invoiceApplication.configuartion;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200", "http://localhost:3000", "http://familyfirstsoftware.com", "http://172.19.0.1", "http://192.168.1.80:8000", "http://192.168.1.80")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Access-Control-Allow-Origin", "Content-Type",
                        "Accept", "Jwt-Token", "Authorization", "Origin", "Accept", "X-Requested-With",
                        "Access-Control-Request-Method", "Access-Control-Request-Headers")
                .exposedHeaders("Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
                        "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "File-Name")
                .allowCredentials(true);
    }
}
