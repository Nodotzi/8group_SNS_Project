package com.sparta.snsproject.controller;

import com.sparta.snsproject.annotation.Sign;
import com.sparta.snsproject.dto.sign.SignUser;
import com.sparta.snsproject.dto.sign.SignoutDto;
import com.sparta.snsproject.dto.sign.SignupRequestDto;
import com.sparta.snsproject.dto.sign.SignupResponseDto;
import com.sparta.snsproject.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(signupRequestDto));
    }

    @DeleteMapping("/api/signout")
    public ResponseEntity<Long> deleteUser(@Sign SignUser signUser, @RequestBody SignoutDto signoutDto) {
        Long id = signUser.getId();
        userService.deleteUser(id, signoutDto);
        return ResponseEntity.noContent().build();
    }
}
