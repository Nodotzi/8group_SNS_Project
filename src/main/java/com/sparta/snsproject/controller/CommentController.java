package com.sparta.snsproject.controller;

import com.sparta.snsproject.annotation.Sign;
import com.sparta.snsproject.dto.comment.CommentRequestDto;
import com.sparta.snsproject.dto.comment.CommentResponseDto;
import com.sparta.snsproject.dto.sign.SignUser;
import com.sparta.snsproject.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@Sign SignUser signUser,@Valid @RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.ok(commentService.createComment(signUser, requestDto));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> getComment( @PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.getComment(commentId));
    }

    @GetMapping("/comments")
    public ResponseEntity<Page<CommentResponseDto>> getMyComments(@Sign SignUser signUser,
                                                                @RequestParam(defaultValue = "1", required = false) int page,
                                                                @RequestParam(defaultValue = "10", required = false) int size) {
        return ResponseEntity.ok(commentService.getUserComments(signUser.getId(), page, size));
    }

    @GetMapping("/comments/posting/{postingId}")
    public ResponseEntity<Page<CommentResponseDto>> getPostingComments(@PathVariable Long postingId,
                                                                @RequestParam(defaultValue = "1", required = false) int page,
                                                                @RequestParam(defaultValue = "10", required = false) int size) {
        return ResponseEntity.ok(commentService.getPostingComments(postingId, page, size));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@Sign SignUser signUser, @PathVariable Long commentId, @Valid @RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.ok(commentService.updateComment(signUser, commentId, requestDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@Sign SignUser signUser, @PathVariable Long commentId) {
        commentService.deleteComment(signUser, commentId);
        return ResponseEntity.noContent().build();
    }
}
