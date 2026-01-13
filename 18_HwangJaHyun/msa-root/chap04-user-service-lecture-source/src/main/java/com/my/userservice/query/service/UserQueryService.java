package com.my.userservice.query.service;

import com.my.userservice.query.dto.UserDTO;
import com.my.userservice.query.dto.UserDetailResponse;
import com.my.userservice.query.dto.UserListResponse;
import com.my.userservice.query.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
 * 사용자 조회 관련 비즈니스 로직을 처리하는 서비스
 * MyBatis Mapper를 사용하여 데이터베이스에서 정보를 조회함
 */
@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserMapper userMapper;

    // 사용자 상세 정보 조회
    public UserDetailResponse getUserDetail(Long userId) {
        UserDTO user = Optional.ofNullable(
                userMapper.findUserById(userId)
        ).orElseThrow(() -> new RuntimeException("유저 정보 찾지 못함"));

        return UserDetailResponse.builder().user(user).build();
    }

    // 전체 사용자 목록 조회
    public UserListResponse getAllUsers() {
        List<UserDTO> users = userMapper.findAllUsers();
        return UserListResponse.builder()
                .users(users)
                .build();
    }

    // 사용자 등급 조회
    public String getUserGrade(Long userId) {
        UserDTO user = userMapper.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("유저 정보 찾지 못함");
        }
        // 예시로 사용자 DTO에 grade 필드가 있다고 가정하고 반환
        // 실제로는 DB에서 조회된 값을 반환해야 함
//        return user.getGrade();
        return "PREMIUM";
    }
}
