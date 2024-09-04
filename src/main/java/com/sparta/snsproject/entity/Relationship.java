package com.sparta.snsproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="relationship")
@NoArgsConstructor
public class Relationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="asking_id", nullable = false)
    private User asking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="asked_id", nullable = false)
    private User asked;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private AskStatus status;

    public Relationship(User asking, User asked) {
        this.asking = asking;
        this.asked = asked;
        this.status = AskStatus.WAIT;
    }

    public void accept() {
        this.status = AskStatus.ACCEPTED;
    }

    public void delete() {
        this.status = AskStatus.DELETED;
    }

    public void cancel() {
        this.status = AskStatus.CANCELLED;
    }
}
