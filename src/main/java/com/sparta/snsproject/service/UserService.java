package com.sparta.snsproject.service;

import com.sparta.snsproject.config.PasswordEncoder;
import com.sparta.snsproject.dto.sign.SignoutDto;
import com.sparta.snsproject.dto.sign.SignupRequestDto;
import com.sparta.snsproject.dto.sign.SignupResponseDto;
import com.sparta.snsproject.dto.user.PasswordUpdateRequestDto;
import com.sparta.snsproject.dto.user.UserProfileRequestDto;
import com.sparta.snsproject.dto.user.UserResponseDto;
import com.sparta.snsproject.dto.user.*;
import com.sparta.snsproject.entity.User;
import com.sparta.snsproject.exception.ChangeSamePasswordException;
import com.sparta.snsproject.exception.DuplicateEmailException;
import com.sparta.snsproject.exception.WrongPasswordException;
import com.sparta.snsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RelationshipService relationshipService;

    @Transactional
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

    //  비밀번호 수정
    @Transactional
    public Long updatePassword(Long id, PasswordUpdateRequestDto passwordUpdateRequestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        User user = find(id);

        //DB password = confirmpassword 확인 //confirm과 newpass 같으면 안되는 예외처리
        if (!passwordEncoder.matches(passwordUpdateRequestDto.getConfirmPassword(), user.getPassword()))
            throw new WrongPasswordException();
        if (passwordUpdateRequestDto.getConfirmPassword().equals(passwordUpdateRequestDto.getNewPassword()))
            throw new ChangeSamePasswordException();
        // user Password 수정
        String encodedPassword = passwordEncoder.encode(passwordUpdateRequestDto.getNewPassword());
        user.updatePassword(encodedPassword);
        return id;
    }

    @Transactional
    public UserProfileResponseDto updateUser(Long id, UserProfileRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        User user = find(id);
        // introduce, nickname 내용 수정
        user.update(requestDto);
        // 사용자 정보 업데이트
        //userService.updateUser(signUser.getId(), requestDto);

        return new UserProfileResponseDto(user);
    }

    private User find(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 유저는 존재하지 않습니다.")
        );
    }

    public UserResponseDto getUser(Long id) {
        User user = find(id);
        return new UserResponseDto(user);
    }

    @Transactional
    public void deleteUser(Long id, SignoutDto signoutDto) {
        //id에 맞는 유저찾기
        User user = userRepository.findById(id).orElseThrow();
        //패스워드가 일치한다면
        if (passwordEncoder.matches(signoutDto.getPassword(), user.getPassword())) {
            //유저 status정보를 ABLE -> DISABLE로
            user.update();
            //탈퇴시 탈퇴유저과 관련된 친구관계, 친구요청 및 대기, 게시글 삭제
            relationshipService.signoutUser(id);
        } else throw new WrongPasswordException();
    }
}
