package com.footstep.domain.posting.repository;

import com.footstep.domain.posting.domain.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
