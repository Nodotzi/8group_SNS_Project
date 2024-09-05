package com.sparta.snsproject.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    @NotNull(message = "게시물의 아이디를 알려주세요")
    private Long postingId;

    @NotBlank(message="내용은 필수 입력값입니다.")
    private String content;
}
