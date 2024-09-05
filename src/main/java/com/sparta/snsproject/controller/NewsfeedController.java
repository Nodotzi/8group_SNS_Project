package com.sparta.snsproject.controller;

import com.sparta.snsproject.annotation.Sign;
import com.sparta.snsproject.dto.NewsfeedRequestDto;
import com.sparta.snsproject.dto.posting.NewsfeedResponseDto;
import com.sparta.snsproject.dto.sign.SignUser;
import com.sparta.snsproject.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NewsfeedController {

    private final PostingService postingService;

    @GetMapping("/api/newsfeed")
    public ResponseEntity<Page<NewsfeedResponseDto>> getNewsfeed(@Sign SignUser signUser, @RequestBody NewsfeedRequestDto requestDto) {
                Long id = signUser.getId();
                int page = requestDto.getPageNumber() - 1;
        return ResponseEntity.ok(postingService.getNewsfeed(id,page));
    }
}
