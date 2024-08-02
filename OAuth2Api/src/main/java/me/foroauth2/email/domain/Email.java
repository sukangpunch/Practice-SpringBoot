package me.foroauth2.email.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="email")
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="email_id")
    private Long email_id;

    @Column(name="email", nullable = false)
    private String email;

    @Column(name="email_status",nullable = false)
    private boolean emailStatus;

    @Builder
    public Email(String email){
        this.email = email;
        this.emailStatus = false;
    }
}
