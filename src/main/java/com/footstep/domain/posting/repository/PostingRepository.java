package com.footstep.domain.posting.repository;

import com.footstep.domain.posting.domain.place.Place;
import com.footstep.domain.posting.domain.posting.Posting;
import com.footstep.domain.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostingRepository extends JpaRepository<Posting, Long> {

    List<Posting> findByUsersAndPlaceOrderByCreatedDateDesc(Users users, Place place);
    List<Posting> findByUsers(Users users);
    List<Posting> findAllByUsersOrderByCreatedDateDesc(Users users);
}
