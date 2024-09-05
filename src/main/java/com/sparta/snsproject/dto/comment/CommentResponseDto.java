package com.sparta.snsproject.dto.comment;

import com.sparta.snsproject.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long id;
    private Long postingId;
    private String content;

    public CommentResponseDto(Comment saveComment) {
        this.id = saveComment.getId();
        this.postingId = saveComment.getPosting().getId();
        this.content = saveComment.getContent();
    }
}
