package com.footstep.domain.posting.repository;

import com.footstep.domain.posting.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
