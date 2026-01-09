package com.my.secondservice.controller;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {
  private Environment env;

  public FirstController(Environment env) {
    this.env = env;
  } // 의존성 주입

  @GetMapping("/health")
  public String healthCheck(){
    return "First Service is Ok. Port = "+env.getProperty("local.server.port");
  }

  @GetMapping("/message")
  public String message(
    @RequestHeader("first-request") String header
  ){
    return "first-request header = "+header;
  }
}
