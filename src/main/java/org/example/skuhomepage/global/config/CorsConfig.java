package org.example.skuhomepage.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/**")
        .allowedOrigins(
            "http://localhost:3000",
            "https://devsku.netlify.app",
            "https://www.skuniv.co.kr",
            "http://localhost:8080",
            "https://api.skuniv.co.kr")
        .allowedMethods("GET", "POST", "PUT", "PATCH", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true)
        .exposedHeaders("Set-Cookie")
        .maxAge(3000);
  }
}
