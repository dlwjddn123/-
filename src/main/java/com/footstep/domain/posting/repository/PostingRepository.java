package com.footstep.domain.posting.repository;

import com.footstep.domain.posting.domain.place.Place;
import com.footstep.domain.posting.domain.posting.Posting;
import com.footstep.domain.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostingRepository extends JpaRepository<Posting, Long> {

    List<Posting> findByUsersAndPlaceOrderByRecordDateDesc(Users users, Place place);
    List<Posting> findByUsers(Users users);
    List<Posting> findAllByUsersOrderByRecordDateDesc(Users users);
    Optional<Posting> findById(Long postingId);
}
