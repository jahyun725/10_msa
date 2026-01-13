package com.my.userservice.auth.service;

import com.my.userservice.auth.dto.LoginRequest;
import com.my.userservice.auth.dto.TokenResponse;
import com.my.userservice.auth.entity.RefreshToken;
import com.my.userservice.auth.repository.AuthRepository;
import com.my.userservice.command.entity.User;
import com.my.userservice.command.repository.UserRepository;
import com.my.userservice.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/*
 * 인증 관련 비즈니스 로직을 처리하는 서비스
 * 로그인, 토큰 발급 및 갱신, 로그아웃 기능을 수행함
 */
@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final AuthRepository authRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  // 로그인 로직: 사용자 검증 후 액세스 토큰 및 리프레시 토큰 발급
  public TokenResponse login(LoginRequest request) {

    User user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new BadCredentialsException("올바르지 않은 아이디 혹은 비밀번호"));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new BadCredentialsException("올바르지 않은 아이디 혹은 비밀번호");
    }

    /* 토큰 생성 시 userId를 추가적으로 전달 */
    String accessToken = jwtTokenProvider.createToken(user.getUsername(), user.getRole().name(), user.getId());
    String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername(), user.getRole().name(), user.getId());

    RefreshToken tokenEntity = RefreshToken.builder()
        .username(user.getUsername())
        .token(refreshToken)
        .expiryDate(
            new Date(System.currentTimeMillis()
                + jwtTokenProvider.getRefreshExpiration())
        )
        .build();

    authRepository.save(tokenEntity);


    return TokenResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  // 토큰 갱신 로직: 리프레시 토큰 유효성 검사 후 새 토큰 발급
  public TokenResponse refreshToken(String providedRefreshToken) {

    jwtTokenProvider.validateToken(providedRefreshToken);
    String username = jwtTokenProvider.getUsernameFromJWT(providedRefreshToken);

    RefreshToken storedToken = authRepository.findById(username)
        .orElseThrow(() -> new BadCredentialsException("해당 유저로 조회되는 리프레시 토큰 없음"));

    if (!storedToken.getToken().equals(providedRefreshToken)) {
      throw new BadCredentialsException("리프레시 토큰 일치하지 않음");
    }

    if (storedToken.getExpiryDate().before(new Date())) {
      throw new BadCredentialsException("리프레시 토큰 유효시간 만료");
    }

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new BadCredentialsException("해당 리프레시 토큰을 위한 유저 없음"));

    String accessToken = jwtTokenProvider.createToken(user.getUsername(), user.getRole().name(), user.getId());
    String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername(), user.getRole().name(), user.getId());

    RefreshToken tokenEntity = RefreshToken.builder()
        .username(user.getUsername())
        .token(refreshToken)
        .expiryDate(
            new Date(System.currentTimeMillis()
                + jwtTokenProvider.getRefreshExpiration())
        )
        .build();

    authRepository.save(tokenEntity);

    return TokenResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();

  }

  // 로그아웃 로직: 리프레시 토큰 삭제
  public void logout(String refreshToken) {
    jwtTokenProvider.validateToken(refreshToken);
    String username = jwtTokenProvider.getUsernameFromJWT(refreshToken);
    authRepository.deleteById(username);
  }
}
