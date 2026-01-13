package com.my.userservice.config;

import com.my.userservice.jwt.HeaderAuthenticationFilter;
import com.my.userservice.jwt.RestAccessDeniedHandler;
import com.my.userservice.jwt.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
 * Spring Security 설정 클래스
 * 인증/인가 정책 설정 및 필터 체인 구성
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
  private final RestAccessDeniedHandler restAccessDeniedHandler;

  // 비밀번호 암호화를 위한 BCryptPasswordEncoder 빈 등록
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        // 세션을 사용하지 않고 Stateless하게 관리함 (JWT 사용)
        .sessionManagement(session
            -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // 예외 처리 핸들러 등록 (인증 실패, 권한 부족)
        .exceptionHandling(exception ->
            exception
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .accessDeniedHandler(restAccessDeniedHandler)
        )
        // URL별 접근 권한 설정
        .authorizeHttpRequests(auth ->
            auth.requestMatchers(HttpMethod.POST, "/users", "/auth/login", "/auth/refresh").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/me").hasAuthority("USER")
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated()
        )

        /* 중요!! */
        // 기존 JWT 검증 필터 대신, Gateway가 전달한 헤더를 이용하는 필터 추가
        .addFilterBefore(headerAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public HeaderAuthenticationFilter headerAuthenticationFilter() {
    return new HeaderAuthenticationFilter();
  }

}
