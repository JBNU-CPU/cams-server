package com.cpu.cams.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry.addMapping("/**")
                .allowedOrigins("http://175.123.55.182", "http://localhost:3000", "http://localhost", "http://127.0.0.1:5500","http://192.168.0.4:3000")
                .exposedHeaders("Authorization"); // ← 새 액세스 토큰 읽으려면 필수
    }
}
