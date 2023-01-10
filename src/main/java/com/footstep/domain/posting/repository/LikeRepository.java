package com.footstep.domain.posting.repository;

import com.footstep.domain.posting.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Likes, Long> {
}
