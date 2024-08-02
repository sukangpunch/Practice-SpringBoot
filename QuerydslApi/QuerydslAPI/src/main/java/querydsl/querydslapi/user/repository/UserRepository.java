package querydsl.querydslapi.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import querydsl.querydslapi.user.domain.User;

public interface UserRepository extends JpaRepository<User,Long>, UserCustomRepository {

}
