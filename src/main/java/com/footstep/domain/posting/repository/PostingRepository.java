package com.footstep.domain.posting.repository;

import com.footstep.domain.posting.domain.posting.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostingRepository extends JpaRepository<Posting, Long> {
}
