package com.ohgiraffers.orderservice.command.client;

import com.ohgiraffers.orderservice.common.ApiResponse;
import com.ohgiraffers.orderservice.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/*
 * User Service와 통신하기 위한 Feign Client 인터페이스
 * @FeignClient: 선언적으로 REST Client를 생성해줌
 */
// 1. Gateway 를 호출하는 상황 (url을 명시하여 Gateway 주소로 요청)
 @FeignClient(name = "user-service", url = "http://localhost:8000", configuration = FeignClientConfig.class)
// 2. 내부에서 user-service를 호출하는 상황 (Eureka에 등록된 서비스 이름으로 호출)
//@FeignClient(name = "swcamp-user-service", configuration = FeignClientConfig.class)
public interface UserClient {

    // User Service에서 사용자 상태나 간단한 정보를 조회하는 API
    // 1. Gateway를 호출하는 상황 (Gateway의 라우팅 경로 포함)
    @GetMapping("/api/v1/user-service/users/{userId}/grade")

    // 2. 내부에서 user-service를 호출하는 상황 (직접 서비스 경로 호출)
//    @GetMapping("/users/{userId}/grade")
    ApiResponse<String> getUserGrade(@PathVariable("userId") Long userId);
}
