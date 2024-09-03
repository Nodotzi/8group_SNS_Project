package com.sparta.snsproject.dto;

import lombok.Getter;
import org.apache.catalina.User;

@Getter
public class PostingSaveResponseDto {

    private final Long id;
    private final String title;
    private final String contents;
    private final String userId;

    public PostingSaveResponseDto(Long id, String title, String contents, String userId) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.userId = userId;

    }

}
