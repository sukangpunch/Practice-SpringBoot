package me.foroauth2.email.repository;

import me.foroauth2.email.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email,Long> {
    Optional<Email> findByEmail(String email);
}
