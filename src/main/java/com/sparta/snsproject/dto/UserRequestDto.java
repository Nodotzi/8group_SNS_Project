package com.sparta.snsproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    private String nickname;
    private String email;
    private String password;
    private String introduce;
}
