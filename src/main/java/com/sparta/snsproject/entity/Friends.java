package com.sparta.snsproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="friends")
@NoArgsConstructor
public class Friends {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="friendA_id", nullable = false)
    private User friendA;

    @ManyToOne
    @JoinColumn(name="friendB_id", nullable = false)
    private User friendB;

    public Friends(User friend1, User friend2) {
        this.friendA = friend1;
        this.friendB = friend2;
    }
}
