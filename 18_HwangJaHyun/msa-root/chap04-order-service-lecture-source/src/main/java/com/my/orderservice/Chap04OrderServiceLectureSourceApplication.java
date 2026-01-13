package com.ohgiraffers.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/*
 * Order Service 메인 애플리케이션 클래스
 * 주문 관리 기능을 담당하는 마이크로서비스
 * @EnableFeignClients: 다른 마이크로서비스(User Service 등)와의 통신을 위해 Feign Client 활성화
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "com.ohgiraffers.orderservice.command.client")
public class Chap04OrderServiceLectureSourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Chap04OrderServiceLectureSourceApplication.class, args);
    }

}
