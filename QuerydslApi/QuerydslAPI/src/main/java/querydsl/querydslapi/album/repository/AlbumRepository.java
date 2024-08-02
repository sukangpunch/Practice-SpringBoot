package querydsl.querydslapi.album.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import querydsl.querydslapi.album.domain.Album;

public interface AlbumRepository extends JpaRepository<Album,Long> {
}
