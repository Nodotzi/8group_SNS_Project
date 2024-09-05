package com.sparta.snsproject.dto.sign;

import com.sparta.snsproject.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignupResponseDto {

    private final Long id;
    private final String email;
    private final String nickname;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public SignupResponseDto(User savedUser) {
        this.id = savedUser.getId();
        this.email = savedUser.getEmail();
        this.nickname = savedUser.getNickname();
        this.createdAt = savedUser.getCreatedAt();
        this.modifiedAt = savedUser.getModifiedAt();
    }
}
