package com.ds.livetest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.service.registry.ImportHttpServices;

@SpringBootApplication
@ImportHttpServices(basePackages = "com.ds.livetest")
public class LiveTestApplication {

  public static void main(String[] args) {
    SpringApplication.run(LiveTestApplication.class, args);
  }
}
