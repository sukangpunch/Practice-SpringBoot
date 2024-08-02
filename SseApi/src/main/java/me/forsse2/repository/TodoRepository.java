package me.forsse2.repository;

import me.forsse2.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long>{

}
