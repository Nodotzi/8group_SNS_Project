package com.sparta.snsproject.controller;

import com.sparta.snsproject.dto.*;
import com.sparta.snsproject.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostingController {

    private final PostingService postingService;

    @PostMapping("/api/posting")
    public ResponseEntity<PostingResponseDto> savePosting(@RequestBody PostingSaveRequestDto postingSaveRequestDto) {
        return ResponseEntity.ok(postingService.savePosting(postingSaveRequestDto));
    }

    @GetMapping("/api/users/{user_id}/postings")
    public ResponseEntity<List<PostingResponseDto>> getPostings() {
        return ResponseEntity.ok(postingService.getPostings());
    }

    @GetMapping("/api/users/{user_id}/posting/{posting_id}")
    public ResponseEntity<PostingResponseDto> getPosting(@PathVariable Long posting_id) {
        return ResponseEntity.ok(postingService.getPosting(posting_id));
    }

    @PutMapping("/api/posting/{posting_id}")
    public ResponseEntity<PostingResponseDto> updatePosting(
            @PathVariable Long posting_id,
            @RequestBody PostingUpdateRequestDto postingUpdateRequestDto
    ) {
        return ResponseEntity.ok(postingService.updatePosting(posting_id, postingUpdateRequestDto));
    }
    
    @DeleteMapping("/api/posting/{posting_id}")
    public void deletePosting(@PathVariable Long posting_id) {
        postingService.deletePosting(posting_id);
    }
}