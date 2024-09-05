package com.sparta.snsproject.dto.user;

import com.sparta.snsproject.entity.User;
import lombok.Getter;

@Getter
    public class UserProfileResponseDto {
    private String Nickname;
    private String Introduce;

    public UserProfileResponseDto(User user) {
        this.Nickname = user.getNickname();
        this.Introduce = user.getIntroduce();
    }
}
