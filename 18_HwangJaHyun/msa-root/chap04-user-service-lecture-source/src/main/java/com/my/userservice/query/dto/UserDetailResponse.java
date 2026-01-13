package com.my.userservice.query.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDetailResponse {
    private com.my.userservice.query.dto.UserDTO user;
}
