package com.sparta.snsproject.dto;

import lombok.Getter;

@Getter
public class PasswordUpdateRequestDto {
    private String newPassword;
    private String confirmPassword;
}
