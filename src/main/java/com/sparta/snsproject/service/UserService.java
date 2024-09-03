package com.sparta.snsproject.service;

import com.sparta.snsproject.config.JwtUtil;
import com.sparta.snsproject.config.PasswordEncoder;
import com.sparta.snsproject.dto.UserRequestDto;
import com.sparta.snsproject.dto.UserResponseDto;
import com.sparta.snsproject.dto.UserSaveResponseDto;
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

    public UserSaveResponseDto createUser(UserRequestDto requestDto) {
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // RequestDto -> entity
        User user = new User(
                requestDto,
                requestDto.getEmail(),
                requestDto.getNickname(), //3Lay Arci 위반인가?
                encodedPassword);
        // DB저장
        User savedUser = userRepository.save(user);
        // Entity -> ResponseDto

        String baererToken = jwtUtil.createToken(
                savedUser.getId(),
                savedUser.getNickname(),
                savedUser.getEmail()
        );
        return new UserSaveResponseDto(baererToken);
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
    public Long deleteUser(Long id) {
        //해당 유저가 DB에 존재하는지 확인
        User user = findUser(id);
        //유저 삭제
        userRepository.delete(user);
        return id;
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



