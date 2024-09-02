package com.sparta.snsproject.controller;

import com.sparta.snsproject.dto.user.UserRequestDto;
import com.sparta.snsproject.dto.user.UserResponseDto;
import com.sparta.snsproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//친구 등록
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 유저 등록
     * @Param 회원가입 정보
     * @Return 유저 등록 정보
     * */
    @PostMapping("")
    public UserResponseDto createUser (@RequestBody UserRequestDto requestDto) {
        return userService.createUser(requestDto);
    }
}
