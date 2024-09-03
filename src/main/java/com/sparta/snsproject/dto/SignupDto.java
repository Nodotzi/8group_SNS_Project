package com.sparta.snsproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SignupDto implements Serializable {

    @Email(message = "이메일 형식을 맞춰주세요.")
    private String email;

    @Pattern(regexp = "^(?=.*?[A-Za-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$",
             message = "대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함하여 최소 8글자 이상이어야 합니다.")
    private String password;

    private String nickname;

    private String introduce;

}
