package querydsl.querydslapi.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import querydsl.querydslapi.album.domain.Album;
import querydsl.querydslapi.profile.domain.Profile;

import java.util.ArrayList;
import java.util.List;


/*
 1. 연관 관계의 주인
 JoinColumn은 연관관계의 주인에서 사용되고, MappedBy는 연관관계의 주인이 아닌 엔티티에서 사용된다.
 2. 매핑 방향
 JoinCoulumn은 단방향 연관 관계에서 사용되며, 외래키를 매핑합니다. MappedBy는 양방향 연관 관계에 사용되며, 양쪽 엔티티 간에 서로를 참조할 수 있도록 매핑합니다.
 3. 관리하는 쪽의 변경 가능성
 JoinCoulmn은 연관관계의 주인에서 외래키를 관리하므로, 외래 키 값이 변경될 때는 항상 관리하는 쪽에서 변경해야합니다.
 MappedBy는 연관관계의 주인이 아닌 엔티티에서 관계를 매핑하므로 관리하는 쪽이 아닌 다른 쪽에서 연관관계를 변경할 수 있습니다.
 4. 필드이름
 JoinCoulumn은 외래 키의 컬럼 이름을 지정하는데 사용하므로, 필드 이름과 다를 수 있습니다. MappedBy는 필드 이름을 그대로 사용하므롤, 필드 이름과 매핑되는 엔티티의  필드 이름이 같아야 합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,name = "user_name")
    private String name;


    //연관관계를 맺고 있는 테이블에서 외래 키를 매핑할 때 사용
    //즉 연관 관계의 주인 에서 외래키를 관리하는 방법이다.
    //Table 유저와, Table 프로필을 join 할떄, join key는 Table유저의 profile_id 컬럼으로 해줘 라는 뜻.
    //referencedColumnName 속성을 생략할 경우 기본값은 참조하는 엔티티의 기본키이다.
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    //user 객체는 Album 테이블을 관리할 수 없고, Album 객체만이 권한을 받고 주인이 아닌쪽은 읽기(조회) 만 가능해진다.
    //Album에서 User 엔티티를 참조하는데 필드를 설정합니다.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Album> albumList = new ArrayList<>();

    @Builder
    protected User(String name, Profile profile)
    {
        this.name = name;
        this.profile = profile;
    }

}
