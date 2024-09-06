package com.sparta.snsproject.controller;

import com.sparta.snsproject.annotation.Sign;
import com.sparta.snsproject.dto.sign.SignUser;
import com.sparta.snsproject.dto.user.PasswordUpdateRequestDto;
import com.sparta.snsproject.dto.user.UserProfileRequestDto;
import com.sparta.snsproject.dto.user.UserProfileResponseDto;
import com.sparta.snsproject.dto.user.UserResponseDto;
import com.sparta.snsproject.entity.User;
import com.sparta.snsproject.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.servlet.function.ServerResponse.noContent;

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
    public UserProfileResponseDto updateUser(@Sign SignUser signUser, @RequestBody UserProfileRequestDto requestDto) {
        return userService.updateUser(signUser.getId(), requestDto);
    }

    //유저 비밀번호 수정
    @PutMapping("/my/password")
    public ResponseEntity<String> updatePassword(@Sign SignUser signUser, @Valid @RequestBody PasswordUpdateRequestDto passwordUpdateRequestDto) {
        Long id = signUser.getId();
        userService.updatePassword(id,passwordUpdateRequestDto);
        return ResponseEntity.ok("암호가 정상적으로 변겅되었습니다.");
    }

}
