package com.footstep.domain.posting.repository;

import com.footstep.domain.posting.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
