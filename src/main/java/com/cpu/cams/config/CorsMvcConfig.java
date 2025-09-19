package com.cpu.cams.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry.addMapping("/**")
                .allowedOrigins("https://admin-tau-ebon.vercel.app","http://localhost:5173/","http://175.123.55.182", "http://localhost:3000", "https://jbnucpu.co.kr", "http://jbnucpu.co.kr", "https://cams.jbnucpu.co.kr", "http://jbnucpu.co.kr")
                .exposedHeaders("Authorization"); // ← 새 액세스 토큰 읽으려면 필수
    }
}
