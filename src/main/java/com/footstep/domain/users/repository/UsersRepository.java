package com.footstep.domain.users.repository;

import com.footstep.domain.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByNickname(String nickname);

    Optional<Users> findByEmail(String email);
}
