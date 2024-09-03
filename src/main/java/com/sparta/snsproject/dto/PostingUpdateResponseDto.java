package com.sparta.snsproject.dto;

import lombok.Getter;

@Getter
public class PostingUpdateResponseDto {
    private final Long id;
    private final String title;
    private final String contents;
    private final String userId;

    public PostingUpdateResponseDto(Long id, String title, String contents, String userId) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.userId = userId;
    }


}
