package com.sparta.snsproject.dto;

import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String email;
    private String password;
    private String nickname;
}
