package com.sparta.snsproject.entity;

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
    @Column(nullable = false, length = 100)
    private String status;
    @Column(length = 100)
    private String nickname;
    @Column(length = 100)
    private String introduce;


    public User(UserRequestDto requestDto, String email, String nickname, String password) {
        this.email = requestDto.getEmail();
        this.password = password;
        this.nickname = nickname;
    }
    public void update(UserRequestDto requestDto){
        this.email = requestDto.getEmail();
        this.password = requestDto.getPassword();
    }

}
