package querydsl.querydslapi.album.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import querydsl.querydslapi.user.domain.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "albums")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "album_name")
    private String albumName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Album(String albumName, User user){
        this.albumName = albumName;
        this.user = user;
    }

}
