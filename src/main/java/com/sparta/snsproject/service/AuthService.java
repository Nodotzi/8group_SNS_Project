package com.sparta.snsproject.service;

import com.sparta.snsproject.config.JwtUtil;
import com.sparta.snsproject.config.PasswordEncoder;
import com.sparta.snsproject.dto.LoginRequestDto;
import com.sparta.snsproject.entity.User;
import com.sparta.snsproject.entity.UserStatusEnum;
import com.sparta.snsproject.exception.NoSignedUserException;
import com.sparta.snsproject.exception.WrongPasswordException;
import com.sparta.snsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new NoSignedUserException());

        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            throw new WrongPasswordException();
        }

        //탈퇴유저 로그인 방지
        if(user.getUser_status().equals(UserStatusEnum.ABLE)) {

            return jwtUtil.createToken(
                    user.getId(),
                    user.getNickname(),
                    user.getEmail()
            );
        }
        else throw new NoSignedUserException();
    }
}
