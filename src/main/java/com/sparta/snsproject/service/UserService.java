package com.sparta.snsproject.service;

import com.sparta.snsproject.config.JwtUtil;
import com.sparta.snsproject.config.PasswordEncoder;
import com.sparta.snsproject.dto.UserRequestDto;
import com.sparta.snsproject.dto.UserResponseDto;
import com.sparta.snsproject.entity.User;
import com.sparta.snsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserResponseDto createUser(UserRequestDto requestDto) {

        boolean okemail = requestDto.getEmail().matches("^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-z]+$");
        boolean okpassword = requestDto.getPassword().matches("^(?=.*?[A-Za-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$");

        if(okpassword && okemail) {
            String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

            //User Entity의 email 설정이 unique라서 중복예외처리 생략

            // RequestDto -> entity
            User user = new User(requestDto, encodedPassword);

            // DB저장
            User savedUser = userRepository.save(user);
            // Entity -> ResponseDto
            return new UserResponseDto(savedUser);
        }
        else throw new IllegalArgumentException("회원가입 정보입력 양식을 만족하지않습니다.");
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
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다.")
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
}