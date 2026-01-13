package com.my.userservice.auth.controller;

import com.my.userservice.auth.dto.LoginRequest;
import com.my.userservice.auth.dto.RefreshTokenRequest;
import com.my.userservice.auth.dto.TokenResponse;
import com.my.userservice.auth.service.AuthService;
import com.my.userservice.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * 인증 관련 API 컨트롤러
 * 로그인, 토큰 갱신, 로그아웃 기능을 제공함
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 로그인 요청 처리
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest request) {
        TokenResponse token = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(token));
    }

    // 리프레시 토큰을 이용한 액세스 토큰 갱신 요청 처리
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(
            @RequestBody RefreshTokenRequest request
            ){
        TokenResponse response = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 로그아웃 요청 처리 (리프레시 토큰 삭제)
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
