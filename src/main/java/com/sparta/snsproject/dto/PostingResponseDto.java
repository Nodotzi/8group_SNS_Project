package com.sparta.snsproject.dto;

import com.sparta.snsproject.entity.User;
import lombok.Getter;

@Getter
public class PostingResponseDto {
    private final Long id;
    private final String title;
    private final String contents;
    private final UserSimpleResponseDto user;

    public PostingResponseDto(Long id, String title, String contents, User user) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.user = new UserSimpleResponseDto(user);
    }



}