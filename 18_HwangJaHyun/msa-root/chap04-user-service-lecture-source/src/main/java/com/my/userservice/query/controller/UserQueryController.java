package com.my.userservice.query.controller;

import com.my.userservice.common.ApiResponse;
import com.my.userservice.query.dto.UserDetailResponse;
import com.my.userservice.query.dto.UserListResponse;
import com.my.userservice.query.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/*
 * 사용자 조회 관련 API 컨트롤러
 * CQRS 패턴 중 Query(조회) 부분을 담당함
 */
@RestController
@RequiredArgsConstructor
public class UserQueryController {

    private final UserQueryService userQueryService;

    // 내 정보 조회
    // @AuthenticationPrincipal: SecurityContext에 저장된 사용자 ID를 주입받음
    @GetMapping("/users/me")
    public ResponseEntity<ApiResponse<UserDetailResponse>> getUserDetail(
            @AuthenticationPrincipal String userId
    ) {
        /* username에 담긴 값이 userId(숫자) */
        UserDetailResponse response = userQueryService.getUserDetail(Long.valueOf(userId));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 전체 사용자 조회 (관리자 전용)
    // @PreAuthorize: 메서드 실행 전 권한 검사
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/users")
    public ResponseEntity<ApiResponse<UserListResponse>> getUsers() {
        UserListResponse response = userQueryService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 사용자 ID로 등급 확인 (BASIC, PREMIUM 등)
    // 다른 마이크로서비스(Order Service 등)에서 호출하여 사용할 수 있음
    @GetMapping("/users/{userId}/grade")
    public ResponseEntity<ApiResponse<String>> getUserGrade(@PathVariable("userId") Long userId) {
        String grade = userQueryService.getUserGrade(userId);
        return ResponseEntity.ok(ApiResponse.success(grade));
    }

}
