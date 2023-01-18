package com.footstep.domain.posting.repository;

import com.footstep.domain.posting.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT COUNT(p) FROM Posting p INNER JOIN Comment c ON p = c.posting WHERE c.status = 'NORMAL' AND p.id = :postingId")
    Integer countByPosting(@Param("postingId")Long postingId);
}
