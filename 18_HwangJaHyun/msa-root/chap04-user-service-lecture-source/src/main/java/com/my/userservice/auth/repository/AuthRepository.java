package com.my.userservice.auth.repository;

import com.my.userservice.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<RefreshToken, String> {
}
