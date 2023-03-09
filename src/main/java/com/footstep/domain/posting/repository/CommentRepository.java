package com.footstep.domain.posting.repository;

import com.footstep.domain.posting.domain.Comment;
import com.footstep.domain.posting.domain.posting.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.status = 'NORMAL' AND c.posting = :posting AND c.id not in :reported")
    List<Comment> findByPosting(@Param("posting") Posting posting, @Param("reported") List<Long> reportedId);

    @Query("SELECT COUNT(p) FROM Posting p INNER JOIN Comment c ON p = c.posting WHERE c.status = 'NORMAL' AND p.id = :postingId")
    Integer countByPosting(@Param("postingId")Long postingId);

    @Override
    @Query("SELECT c FROM Comment c WHERE c.status = 'NORMAL' AND c.id = :commentId")
    Optional<Comment> findById(@Param("commentId") Long commentId);
}
