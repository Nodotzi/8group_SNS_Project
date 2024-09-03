package com.sparta.snsproject.controller;

import com.sparta.snsproject.dto.UserRequestDto;
import com.sparta.snsproject.dto.UserResponseDto;
import com.sparta.snsproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignController {
    private final UserService userService;

    @PostMapping("/api/signup")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto requestDto) {
        return ResponseEntity.ok(userService.createUser(requestDto));
    }

//    @DeleteMapping("/api/signout")
//    public ResponseEntity<Object> deleteUser()
}
