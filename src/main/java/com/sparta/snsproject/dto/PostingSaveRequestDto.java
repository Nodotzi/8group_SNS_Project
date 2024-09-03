package com.sparta.snsproject.dto;

import lombok.Getter;

@Getter
public class PostingSaveRequestDto {

    private String userId;
    private String title;
    private String contents;
}
