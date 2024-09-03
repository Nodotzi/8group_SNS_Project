package com.sparta.snsproject.entity;

import com.sparta.snsproject.dto.SignupDto;
import com.sparta.snsproject.dto.UserRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String email;
    @Column(nullable = false, length = 100)
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 100)
    private UserStatusEnum user_status = UserStatusEnum.ABLE;

    @Column(nullable = false, length = 100)
    private String nickname;
    @Column(length = 100)
    private String introduce;

    public User(SignupDto requestDto, String password) {
        this.email = requestDto.getEmail();
        this.password = password;
        this.nickname = requestDto.getNickname();
        this.introduce = requestDto.getIntroduce();
    }
    public void update(UserRequestDto requestDto){
        this.email = requestDto.getEmail();
        this.password = requestDto.getPassword();
    }

    public void update() {
        this.user_status = UserStatusEnum.DISABLE;
    }
}
