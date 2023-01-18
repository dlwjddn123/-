package com.footstep.domain.posting.repository;

import com.footstep.domain.posting.domain.Comment;
import com.footstep.domain.posting.domain.posting.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPosting(Posting posting);
    @Query("SELECT COUNT(p) FROM Posting p INNER JOIN Comment c ON p = c.posting WHERE c.status = 'NORMAL' AND p.id = :postingId")
    Integer countByPosting(@Param("postingId")Long postingId);
}
