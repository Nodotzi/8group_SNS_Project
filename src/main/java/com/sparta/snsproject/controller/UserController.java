package com.sparta.snsproject.controller;

import com.sparta.snsproject.annotation.Sign;
import com.sparta.snsproject.dto.*;
import com.sparta.snsproject.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    //유저정보 단건 조회
    @GetMapping("/users/{id}")
    public UserResponseDto getUsers(@PathVariable Long id) {
        return userService.getUser(id);
    }
    //유저 사용자정보 수정
    @PutMapping("/my/profile") //반환타입 메서드
    public UserResponseDto updateUser(@PathVariable Long id, @RequestBody UserRequestDto requestDto) {
        return userService.getUser(userService.updateUser(id, requestDto));
    }
    //유저 비밀번호 수정
    @PostMapping("/my/password")
    public ResponseEntity<Long> updatePassword(@Sign SignUser signUser, @Valid @RequestBody PasswordUpdateRequestDto passwordUpdateRequestDto) {
        Long id = signUser.getId();
        return ResponseEntity.ok(userService.updatePassword(id,passwordUpdateRequestDto));
    }

}
