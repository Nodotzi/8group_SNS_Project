package com.sparta.snsproject.controller;

import com.sparta.snsproject.dto.UserRequestDto;
import com.sparta.snsproject.dto.UserResponseDto;
import com.sparta.snsproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    //유저정보 단건 조회
    @PostMapping("/{id}")
    public UserResponseDto getUsers(@PathVariable Long id) {
        return userService.getUser(id);
    }
    //유저 사용자정보 수정
    @PutMapping("/{id}") //반환타입 메서드
    public UserResponseDto updateUser(@PathVariable Long id, @RequestBody UserRequestDto requestDto) {
        return userService.getUser(userService.updateUser(id, requestDto));
    }
    //유저 비밀번호 수정
}
