package com.my.secondservice.controller;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecondController {
  private Environment env;

  public SecondController(Environment env) {
    this.env = env;
  } // 의존성 주입

  @GetMapping("/health")
  public String healthCheck(){
    return "Second Service is Ok. Port = "+env.getProperty("local.server.port");
  }

  @GetMapping("/message")
  public String message(
    @RequestHeader("second-request") String header
  ){
    return "second-request header = "+header;
  }
}
