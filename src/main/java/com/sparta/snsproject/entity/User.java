package com.sparta.snsproject.entity;

import com.sparta.snsproject.dto.user.UserRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="user")
@NoArgsConstructor
public class User extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="email", unique = true, length = 100)
    private String email;

    @Column(name="password", nullable = false, length = 100)
    private String password;

    @Column(name="status", nullable = false, length = 100)
    private UserStatus status;

    @Column(name="nickname", unique = true, length = 100)
    private String nickname;

    @Column(name="introduce", length=100)
    private String introduce;

    @OneToMany(mappedBy = "asking", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Relationship> askings = new ArrayList<>();

    @OneToMany(mappedBy = "asked", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Relationship> askeds = new ArrayList<>();

    @OneToMany(mappedBy = "friendB",cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Friends> friendBs = new ArrayList<>();

    public User(UserRequestDto requestDto) {
        this.email = requestDto.getEmail();
        this.password = requestDto.getPassword();
        this.nickname = requestDto.getNickname();
        this.status = UserStatus.ABLED;
    }
}
