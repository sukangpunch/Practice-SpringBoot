package querydsl.querydslapi.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import querydsl.querydslapi.profile.domain.Profile;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
}
