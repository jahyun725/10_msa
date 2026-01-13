package com.my.userservice.auth.service;

import com.my.userservice.auth.model.CustomUser;
import com.my.userservice.command.entity.User;
import com.my.userservice.command.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/*
 * Spring Security에서 로그인 처리를 위해 사용자 정보를 조회하는 서비스
 * UserDetailsService 인터페이스를 구현하여 loadUserByUsername 메서드를 오버라이드해야 함
 */
//@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  /*
   * 로그인 시 입력된 username(아이디)을 기반으로 DB에서 사용자 정보를 조회하는 메서드
   * Spring Security의 AuthenticationManager가 인증을 수행할 때 내부적으로 호출됨
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // 1. DB에서 username으로 사용자 조회
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("유저 찾지 못함"));

    /* 수정 사항 : spring security의 UserDetails를 extends 하여 CustomUser 작성
     * 고유 id 숫자, 로그인 id(username), password, authorities 를 저장하고 반환
     * 2. 조회된 사용자 정보를 기반으로 UserDetails 구현체(CustomUser) 생성하여 반환
     *    이 객체는 이후 패스워드 검증 등에 사용됨
     */
    return CustomUser.builder()
        .id(user.getId())
        .username(user.getUsername())
        .password(user.getPassword())
        .authorities(Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())))
        .build();
  }
}
