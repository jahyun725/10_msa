package com.ohgiraffers.orderservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
 * Spring Security 설정 클래스 (Order Service용)
 * Gateway로부터 전달받은 헤더 정보를 기반으로 인증을 처리함
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
  private final RestAccessDeniedHandler restAccessDeniedHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        // 세션을 사용하지 않고 Stateless하게 관리함
        .sessionManagement(session
            -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // 예외 처리 핸들러 등록
        .exceptionHandling(exception ->
            exception
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .accessDeniedHandler(restAccessDeniedHandler)
        )
        // URL별 접근 권한 설정
        .authorizeHttpRequests(auth ->
            auth.requestMatchers(HttpMethod.POST, "/orders").hasAuthority("USER")
                .anyRequest().authenticated()
        )

        /* 중요!!! */
        // 기존 JWT 검증 필터 대신, Gateway가 전달한 헤더를 이용하는 필터 추가
        .addFilterBefore(headerAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public HeaderAuthenticationFilter headerAuthenticationFilter() {
    return new HeaderAuthenticationFilter();
  }

}
