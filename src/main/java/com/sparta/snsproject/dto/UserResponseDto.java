package com.sparta.snsproject.dto;

import com.sparta.snsproject.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private Long id;
    private String email;
    private String nickname;

    public UserResponseDto(User savedUser) {
        this.id = savedUser.getId();
        this.email = savedUser.getEmail();
        this.nickname = savedUser.getNickname();
    }
}
