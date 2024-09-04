package com.sparta.snsproject.dto;

import com.sparta.snsproject.entity.Posting;
import lombok.Getter;

@Getter
public class NewsfeedResponseDto {

    private final String title;
    private final String contents;

    public NewsfeedResponseDto(Posting posting) {
        this.title = posting.getTitle();
        this.contents = posting.getContents();
    }
}
