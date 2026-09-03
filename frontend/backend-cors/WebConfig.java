// src/main/java/com/fintech/api/config/WebConfig.java
package com.fintech.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Vite dev server (localhost:5173) өөр порт дээр ажилладаг тул
// browser CORS policy-ийн улмаас хүсэлтийг блоклоно. Энэ config
// зөвхөн frontend-ийн хаягаас ирэх хүсэлтийг зөвшөөрнө.
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
