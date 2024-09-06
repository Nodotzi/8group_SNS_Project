package com.sparta.snsproject.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentUpdateRequestDto {

    @NotBlank(message="내용은 필수 입력값입니다.")
    private String content;
}
