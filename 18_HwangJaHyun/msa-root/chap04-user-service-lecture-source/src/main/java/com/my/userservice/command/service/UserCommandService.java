package com.my.userservice.command.service;

import com.my.userservice.command.dto.UserCreateRequest;
import com.my.userservice.command.entity.User;
import com.my.userservice.command.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 * 사용자 변경 관련 비즈니스 로직을 처리하는 서비스
 * 회원 가입 시 비밀번호 암호화 등을 수행함
 */
@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입 로직
    @Transactional
    public void registerUser(UserCreateRequest request) {
       // 중복 회원 체크 로직 등 추가 가능
       User user = modelMapper.map(request, User.class);
       // 비밀번호 암호화 후 저장
       user.setEncodedPassword(passwordEncoder.encode(request.getPassword()));
       userRepository.save(user);
    }
}
