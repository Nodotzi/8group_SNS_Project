package com.sparta.snsproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Posting extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String title;
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;



    public Posting(String title, String contents, String userId) {
        this.title = title;
        this.contents = contents;
        this.userId = userId;
    }


    public void update(String contents, String title) {
        this.contents = contents;
        this.title = title;
    }


}

