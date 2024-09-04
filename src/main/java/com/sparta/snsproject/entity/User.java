package com.sparta.snsproject.entity;

import com.sparta.snsproject.dto.SignoutDto;
import com.sparta.snsproject.dto.SignupRequestDto;
import com.sparta.snsproject.dto.UserRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false, length = 100)
    private String nickname;
    @Column(length = 100)
    private String introduce;

    @OneToMany(mappedBy = "send", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Relationship> sends = new ArrayList<>();

    @OneToMany(mappedBy = "receive", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Relationship> receives = new ArrayList<>();

    @OneToMany(mappedBy = "friendB",cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Friends> friendBs = new ArrayList<>();



    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Posting> postingList = new ArrayList<>();

    public User(SignupRequestDto requestDto, String password) {
        this.email = requestDto.getEmail();
        this.password = password;
        this.nickname = requestDto.getNickname();
        this.introduce = requestDto.getIntroduce();
    }

    public void update(UserRequestDto requestDto){
    }
    public void updatePassword(String password){
        this.password = password;
    }
    public void update() {
        this.user_status = UserStatusEnum.DISABLE;
    }
}
