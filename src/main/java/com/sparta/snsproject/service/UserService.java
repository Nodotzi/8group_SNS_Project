package com.sparta.snsproject.service;

import com.sparta.snsproject.annotation.Sign;
import com.sparta.snsproject.config.JwtUtil;
import com.sparta.snsproject.config.PasswordEncoder;
import com.sparta.snsproject.dto.*;
import com.sparta.snsproject.entity.User;
import com.sparta.snsproject.exception.DuplicateEmailException;
import com.sparta.snsproject.exception.GlobalExceptionHandler;
import com.sparta.snsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserResponseDto createUser(SignupDto requestDto) {

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        //User Entity의 email 설정이 unique라서 중복예외처리 생략
        Optional<User> existingUser = userRepository.findByEmail(requestDto.getEmail());
        if (existingUser.isPresent()) {
            throw new DuplicateEmailException();
        }

        // RequestDto -> entity
        User user = new User(requestDto, encodedPassword);

        // DB저장
        User savedUser = userRepository.save(user);
        // Entity -> ResponseDto
        return new UserResponseDto(savedUser);
    }

    public List<UserResponseDto> getAllUser() {
        return userRepository.findAll().stream().map(UserResponseDto::new).collect(Collectors.toList());
    } // 메서드 이름으로 SQL 생성하는 Query Methods 기능.

    public Long updateUser(Long id, UserRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        User user = findUser(id);
        // schedule 내용 수정
        user.update(requestDto);

        return id;
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 유저는 존재하지 않습니다.")
        );
    }

    public UserResponseDto getUser(Long id) {
        User user = find(id);
        UserResponseDto responseDto = new UserResponseDto(user);
        return responseDto;
    }

    public User find(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public Long deleteUser(Long id, SignoutDto signoutDto) {
        User user = userRepository.findById(id).orElseThrow();
        if(passwordEncoder.matches(signoutDto.getPassword(), user.getPassword())) {
            user.update();
            return id;
        }
        else throw new IllegalArgumentException("비밀번호가 일치하지않습니다.");
    }
}
