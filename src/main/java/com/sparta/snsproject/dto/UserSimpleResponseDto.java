package com.sparta.snsproject.dto;

import com.sparta.snsproject.entity.User;
import lombok.Getter;

@Getter
public class UserSimpleResponseDto {
    private Long id;
    private String nickname;

    public UserSimpleResponseDto(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
    }
}
