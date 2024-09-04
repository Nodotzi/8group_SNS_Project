package com.sparta.snsproject.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class PasswordUpdateRequestDto {
    @Pattern(regexp = "^(?=.*?[A-Za-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$",
            message = "대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함하여 최소 8글자 이상이어야 합니다.")
    private String newPassword;
    private String confirmPassword;
}
