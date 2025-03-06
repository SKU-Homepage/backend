package org.example.skuhomepage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SkuHomepageApplication {

  public static void main(String[] args) {
    SpringApplication.run(SkuHomepageApplication.class, args);
  }
}
