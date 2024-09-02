package com.sparta.snsproject.dto.user;

import com.sparta.snsproject.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private Long id;
    private String email;
    private String password;

    public UserResponseDto(User savedUser) {
        this.id = savedUser.getId();
        this.email = savedUser.getEmail();
        this.password = savedUser.getPassword();
    }
}
