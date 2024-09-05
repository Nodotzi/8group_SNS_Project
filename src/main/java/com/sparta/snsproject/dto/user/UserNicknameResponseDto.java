package com.sparta.snsproject.dto.user;

import com.sparta.snsproject.entity.User;
import lombok.Getter;

@Getter
public class UserNicknameResponseDto {

    private String nickname;

    public UserNicknameResponseDto(User user) {
        this.nickname = user.getNickname();
    }
}