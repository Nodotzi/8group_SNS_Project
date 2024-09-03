package com.sparta.snsproject.dto;

import lombok.Getter;


@Getter
public class UserSaveResponseDto {
    private final String bearerToken;


    public UserSaveResponseDto(String BearerToken) {
        this.bearerToken = BearerToken;
    }
}
