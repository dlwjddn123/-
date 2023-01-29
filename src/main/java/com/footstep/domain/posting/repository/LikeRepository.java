package com.footstep.domain.posting.repository;

import com.footstep.domain.posting.domain.Likes;
import com.footstep.domain.posting.domain.posting.Posting;
import com.footstep.domain.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Optional<Integer> countByPosting(Posting posting);

    Optional<Likes> findByUsers(Users users);

    @Query("SELECT l FROM Likes l WHERE l.users = :users AND l.posting = :posting")
    Optional<Likes> findByUsersAndPosting(@Param("users") Users users, @Param("posting") Posting posting);
}