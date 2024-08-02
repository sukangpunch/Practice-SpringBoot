package me.foroauth2.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.foroauth2.common.domain.BaseTimeEntity;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name="user_name", nullable = false)
    private String name;

    @Column(name = "user_email", nullable = false)
    private String email;

    @Column(name = "oauth_provider")
    private String provider;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private List<String> role;

    @Builder
    public User(String provider,String name, String email, List<String> role)
    {
        this.provider = provider;
        this.name = name;
        this.email = email;
        this.role = role;
    }

}
