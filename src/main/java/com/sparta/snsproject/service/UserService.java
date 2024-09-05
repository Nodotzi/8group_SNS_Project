package com.sparta.snsproject.service;

import com.sparta.snsproject.config.PasswordEncoder;
import com.sparta.snsproject.dto.sign.SignoutDto;
import com.sparta.snsproject.dto.sign.SignupRequestDto;
import com.sparta.snsproject.dto.sign.SignupResponseDto;
import com.sparta.snsproject.dto.user.PasswordUpdateRequestDto;
import com.sparta.snsproject.dto.user.UserRequestDto;
import com.sparta.snsproject.dto.user.UserResponseDto;
import com.sparta.snsproject.entity.User;
import com.sparta.snsproject.exception.DuplicateEmailException;
import com.sparta.snsproject.exception.WrongPasswordException;
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
    private final RelationshipService relationshipService;

    public SignupResponseDto createUser(SignupRequestDto requestDto) {

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        //User Entity의 email 중복예외처리
        Optional<User> existingUser = userRepository.findByEmail(requestDto.getEmail());
        if (existingUser.isPresent()) {
            throw new DuplicateEmailException();
        }

        // RequestDto -> entity
        User user = new User(requestDto, encodedPassword);

        // DB저장
        User savedUser = userRepository.save(user);
        // Entity -> ResponseDto
        return new SignupResponseDto(savedUser);
    }

    public List<UserResponseDto> getAllUser() {
        return userRepository.findAll().stream().map(UserResponseDto::new).collect(Collectors.toList());
    } // 메서드 이름으로 SQL 생성하는 Query Methods 기능.

    public Long updatePassword(Long id, PasswordUpdateRequestDto passwordUpdateRequestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        User user = find(id);

        //DB password = confirmpassword 확인
        if(passwordEncoder.matches(passwordUpdateRequestDto.getConfirmPassword(), user.getPassword())){
            // user Password 수정
            String encodedPassword = passwordEncoder.encode(passwordUpdateRequestDto.getNewPassword());
            user.updatePassword(encodedPassword);
        }
        else{
            throw new WrongPasswordException();
        }

        return id;
    }
    public Long updateUser(Long id, UserRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        User user = find(id);
        // introduce, nickname 내용 수정
        user.update(requestDto);

        return id;

    }

    private User find(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 유저는 존재하지 않습니다.")
        );
    }

    public UserResponseDto getUser(Long id) {
        User user = find(id);
        UserResponseDto responseDto = new UserResponseDto(user);
        return responseDto;
    }
    
    public Long deleteUser(Long id, SignoutDto signoutDto) {
        User user = userRepository.findById(id).orElseThrow();
        if(passwordEncoder.matches(signoutDto.getPassword(), user.getPassword())) {
            user.update();
            relationshipService.SignoutUser(id);
            return id;
        }
        else throw new WrongPasswordException();
    }
}
