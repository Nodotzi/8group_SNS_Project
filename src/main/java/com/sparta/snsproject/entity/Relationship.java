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
    @JoinColumn(name="send_id", nullable = false)
    private User send;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="receive_id", nullable = false)
    private User receive;

    @Column(name="status")
    @Enumerated(value = EnumType.STRING)
    private AskStatus status;

    public Relationship(User send, User receive) {
        this.send = send;
        this.receive = receive;
        this.status = AskStatus.WAIT;
    }

    public void accept() {
        this.status = AskStatus.ACCEPTED;
    }
}
