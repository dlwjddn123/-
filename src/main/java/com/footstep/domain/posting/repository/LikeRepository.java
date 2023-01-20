package com.footstep.domain.posting.repository;

import com.footstep.domain.posting.domain.Likes;
import com.footstep.domain.posting.domain.posting.Posting;
import com.footstep.domain.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByUsersAndPosting(Users user, Posting posting);

    Optional<Integer> countByPosting(Posting posting);

    Optional<Likes> findByUsers(Users users);
}