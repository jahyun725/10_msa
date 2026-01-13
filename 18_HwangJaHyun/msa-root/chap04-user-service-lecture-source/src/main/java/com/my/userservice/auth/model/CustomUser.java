package com.my.userservice.auth.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/*
 * Spring Security에서 사용할 사용자 정보 객체 (UserDetails 구현체)
 * 인증 과정에서 DB에서 조회된 사용자 정보를 Spring Security가 이해할 수 있는 형태로 변환하여 담고 있음
 */
@Getter
@Builder
public class CustomUser implements UserDetails {

    private final Long id;          // DB의 PK (비즈니스 로직에서 사용하기 위해 추가)
    private final String username;  // 로그인 ID
    private final String password;  // 암호화된 비밀번호
    private final Collection<? extends GrantedAuthority> authorities; // 권한 목록

    /*
     * 계정 만료 여부
     * true : 만료 안됨
     * false : 만료됨
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /*
     * 계정 잠김 여부
     * true : 잠기지 않음
     * false : 잠김
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /*
     * 비밀번호 만료 여부
     * true : 만료 안됨
     * false : 만료됨
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /*
     * 계정 활성화 여부
     * true : 활성화
     * false : 비활성화
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
