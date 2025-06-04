package io.github.cwacoderwithattitude.games.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.cwacoderwithattitude.games.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    io.github.cwacoderwithattitude.games.model.User findByUsername(String username);

    boolean existsByUsername(String username);
}
