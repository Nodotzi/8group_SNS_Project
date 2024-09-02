package com.sparta.snsproject.service;

import com.sparta.snsproject.dto.user.UserRequestDto;
import com.sparta.snsproject.dto.user.UserResponseDto;
import com.sparta.snsproject.entity.User;
import com.sparta.snsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    //유저 등록
    @Transactional
    public UserResponseDto createUser(UserRequestDto requestDto) {
        //등록 정보로 유저 객체 생성
        User user = new User(requestDto);
        //유저 데이터 저장
        User savedUser = userRepository.save(user);
        //저장된 유저 데이터를 responseDto로 보냄
        return new UserResponseDto(savedUser);
    }
}
