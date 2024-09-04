package com.sparta.snsproject.dto;

import com.sparta.snsproject.entity.User;
import lombok.Getter;

@Getter
public class PostingSaveRequestDto {
    private Long userId;
    private String title;
    private String contents;
}
