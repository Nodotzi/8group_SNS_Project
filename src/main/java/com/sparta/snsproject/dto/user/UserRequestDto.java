package com.sparta.snsproject.dto.user;

import lombok.Getter;

@Getter
public class UserRequestDto {
    private String nickname;
    private String email;
    private String password;
    private String introduce;
}
