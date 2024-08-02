package querydsl.querydslapi.profile.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import querydsl.querydslapi.user.domain.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name ="profile_name")
    private String profileName;

    @Column(nullable = false,name ="profile_path")
    private String profilePath;

    @OneToOne(mappedBy = "profile")
    private User user;

    @Builder
    public Profile(String profileName, String profilePath)
    {
        this.profileName = profileName;
        this.profilePath = profilePath;
    }
}
