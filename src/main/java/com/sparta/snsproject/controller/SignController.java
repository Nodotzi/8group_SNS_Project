package com.sparta.snsproject.controller;

import com.sparta.snsproject.annotation.Sign;
import com.sparta.snsproject.dto.sign.SignUser;
import com.sparta.snsproject.dto.sign.SignoutDto;
import com.sparta.snsproject.dto.sign.SignupRequestDto;
import com.sparta.snsproject.dto.sign.SignupResponseDto;
import com.sparta.snsproject.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignController {
    private final UserService userService;

    @PostMapping("/api/signup")
    public ResponseEntity<SignupResponseDto> createUser(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity.ok(userService.createUser(signupRequestDto));
    }

    @DeleteMapping("/api/signout")
    public ResponseEntity<Long> deleteUser(@Sign SignUser signUser, @RequestBody SignoutDto signoutDto) {
        Long id = signUser.getId();
        return ResponseEntity.ok(userService.deleteUser(id,signoutDto));
    }
}
