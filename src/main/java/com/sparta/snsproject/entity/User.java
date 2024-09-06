package com.sparta.snsproject.entity;

import com.sparta.snsproject.dto.sign.SignupRequestDto;
import com.sparta.snsproject.dto.user.UserProfileRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class User extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 100)
    private String email;
    @Column(nullable = false, length = 100)
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 100)
    private UserStatusEnum user_status = UserStatusEnum.ABLE;

    @Column(unique = true, length = 100)
    private String nickname;
    @Column(length = 100)
    private String introduce;

    public User(SignupRequestDto requestDto, String password) {
        this.email = requestDto.getEmail();
        this.password = password;
        this.nickname = requestDto.getNickname();
        this.introduce = requestDto.getIntroduce();
    }

    public void update(UserProfileRequestDto requestDto) {
        this.nickname = requestDto.getNewNickname();
        this.introduce = requestDto.getNewIntroduce();
    }
    public void updatePassword(String password){
        this.password = password;
    }
    public void update() {
        this.user_status = UserStatusEnum.DISABLE;
    }
}
