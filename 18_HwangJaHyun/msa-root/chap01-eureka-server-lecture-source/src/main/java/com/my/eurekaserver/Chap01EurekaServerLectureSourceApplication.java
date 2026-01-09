package com.my.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer /* 해당 애플리케이션을 Eureka Server로 활성 */
public class Chap01EurekaServerLectureSourceApplication {

  public static void main(String[] args) {
    SpringApplication.run(Chap01EurekaServerLectureSourceApplication.class, args);
  }

}
