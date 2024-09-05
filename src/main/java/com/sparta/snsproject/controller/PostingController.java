package com.sparta.snsproject.controller;

import com.sparta.snsproject.annotation.Sign;
import com.sparta.snsproject.dto.posting.PostingRequestDto;
import com.sparta.snsproject.dto.posting.PostingResponseDto;
import com.sparta.snsproject.dto.sign.SignUser;
import com.sparta.snsproject.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostingController {

    private final PostingService postingService;

    //게시물 작성
    @PostMapping("/api/posting")
    public ResponseEntity<PostingResponseDto> savePosting(@Sign SignUser signUser, @RequestBody PostingRequestDto postingRequestDto) {
        return ResponseEntity.ok(postingService.savePosting(signUser, postingRequestDto));
    }

    //사용자의 모든 게시물 조회
    @GetMapping("/api/postings")
    public ResponseEntity<List<PostingResponseDto>> getPostings(@Sign SignUser signUser) {
        return ResponseEntity.ok(postingService.getPostings(signUser));
    }

    //특정 게시물 단건 조회
    @GetMapping("/api/posting/{posting_id}")
    public ResponseEntity<PostingResponseDto> getPosting(@PathVariable Long posting_id) {
        return ResponseEntity.ok(postingService.getPosting(posting_id));
    }

    //게시물 수정
    @PutMapping("/api/posting/{posting_id}")
    public ResponseEntity<PostingResponseDto> updatePosting(
            @PathVariable Long posting_id,
            @RequestBody PostingRequestDto postingRequestDto
    ) {
        return ResponseEntity.ok(postingService.updatePosting(posting_id, postingRequestDto));
    }

    //게시물 삭제
    @DeleteMapping("/api/posting/{posting_id}")
    public ResponseEntity<Void> deletePosting(@PathVariable Long posting_id) {
        postingService.deletePosting(posting_id);
        return ResponseEntity.noContent().build();
    }
}