package com.sparta.snsproject.dto;

import com.sparta.snsproject.entity.User;
import lombok.Getter;

@Getter
public class PostingSimpleResponseDto {

    private final Long id;
    private final String title;
    private final String contents;
    private final User user;

    public PostingSimpleResponseDto(Long id, String title, String contents, User user) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.user = user;
    }



}
