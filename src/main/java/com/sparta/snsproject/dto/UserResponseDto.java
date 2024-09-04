package com.sparta.snsproject.dto;

import com.sparta.snsproject.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {
    private final Long userId;
    private final String email;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    //리스트 만들기


    public UserResponseDto(User savedUser) {
        this.userId = savedUser.getId();
        this.email = savedUser.getEmail();
        this.createdAt = savedUser.getCreatedAt();
        this.modifiedAt = savedUser.getModifiedAt();
    }


}
