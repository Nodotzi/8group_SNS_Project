package com.sparta.snsproject.controller;

import com.sparta.snsproject.annotation.Sign;
import com.sparta.snsproject.dto.NewsfeedResponseDto;
import com.sparta.snsproject.dto.SignUser;
import com.sparta.snsproject.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NewsfeedController {

    private final PostingService postingService;

    @GetMapping("/api/newsfeed")
    public Page<NewsfeedResponseDto> getNewsfeed(@Sign SignUser signUser) {
                Long id = signUser.getId();
        return postingService.getNewsfeed(id);
    }
}
