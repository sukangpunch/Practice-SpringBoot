package com.example.redisproject.data.domain;

import com.example.redisproject.common.domain.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
// 아무런 매개변수가 없는 생성자를 생성하되 다른 패키지에 소속된 클래스는 접근을 불허한다
// 엔티티의 연관 관계에서 지연 로딩의 경우에는 실제 엔티티가 아닌 프록시 객체를 통해서 조회
// Private로 하면 프록시 객체 생성에 문제가 생기고,
// Public으로 하면 무분별한 객체 생성 및 setter를 통한 값 주입이 가능해진다
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name",nullable = false)
    private String name;

    @Column(name = "user_email",nullable = false)
    private String email;

    // password 필드가 JSON 직렬화 시에만 사용되고, 역직렬화 시에는 무시되도록 설정합니다.
    // 이렇게 함으로써 클라이언트로부터 전송된 비밀번호는 서버에서만 필요한 인증 및 저장 과정에서만 사용되고,
    // API 응답에는 포함되지 않습니다.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_role", nullable = false)
    private String role;

    @Builder
    protected User(String name, String email, String password, String role){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }


    // SimpleGrantedAuthority
    // -> Spring Security에서 사용되는 권한 정보를 담는 클래스입니다.
    // 주로 사용자가 가진 권한(역할)을 나타내며, 보통은 문자열로 표현된 권한 이름을 포함합니다.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(this.role));
    }

    // 사용자의 loginId를 반환하는 메서드,
    //일반적으로 외부에 노출되어도 되는 정보이기 때문에 JsonProperty.Access.WRITE_ONLY 가 필요하지 않다.
    @Override
    public String getUsername() {
        return this.email;
    }

    //사용자 계정의 만료 여부
    //false를 반환하면 사용자 계정이 만료되었다는 뜻
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //사용자 계정이 잠겨있는지 여부
    //false를 반환하면 계정이 잠겨있다는 뜻
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    //사용자의 자격증명(패스워드 등) 만료 여부
    //false를 반환하면 자격 증명이 만료되었다는 뜻
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //사용자의 계정 활성화 여부
    //false를 반환하면 계정이 비활성화 되었다는 뜻
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
