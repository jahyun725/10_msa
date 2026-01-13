package com.my.userservice.command.controller;

import com.my.userservice.command.dto.UserCreateRequest;
import com.my.userservice.command.service.UserCommandService;
import com.my.userservice.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * 사용자 등록/수정/삭제 등 변경 관련 API 컨트롤러
 * CQRS 패턴 중 Command(명령) 부분을 담당함
 */
@RestController
@RequiredArgsConstructor
public class UserCommandController {

    private final UserCommandService userCommandService;

    // 회원 가입 요청 처리
    @PostMapping("/users")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody UserCreateRequest request) {
        userCommandService.registerUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

}
