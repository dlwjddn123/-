package com.footstep.domain.posting.repository;

import com.footstep.domain.posting.domain.place.Place;
import com.footstep.domain.posting.domain.posting.Posting;
import com.footstep.domain.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface PostingRepository extends JpaRepository<Posting, Long> {

    @Query("SELECT p FROM Posting p WHERE p.status = 'NORMAL' AND p.users = :users AND " +
            "p.place = :place ORDER BY p.recordDate DESC")
    List<Posting> findByUsersAndPlaceOrderByRecordDateDesc(@Param("users") Users users, @Param("place") Place place);

    @Query("SELECT p FROM Posting p WHERE p.status = 'NORMAL' AND p.users = :users ORDER BY p.recordDate DESC")
    List<Posting> findByUsers(@Param("users") Users users);

    @Query("SELECT p FROM Posting p WHERE p.status = 'NORMAL' AND p.users != :users ORDER BY p.recordDate DESC")
    List<Posting> findAllFeed(@Param("users") Users users);

    @Query("SELECT p FROM Posting p WHERE p.status = 'NORMAL' AND p.users = :users AND p.recordDate = :date")
    List<Posting> findByUsersAndRecordDate(@Param("users") Users users, @Param("date") Date date);

    @Query("SELECT p FROM Posting p WHERE p.status = 'NORMAL' AND p.users = :users AND p.place = :place AND p.recordDate = :date")
    List<Posting> findByUsersAndRecordDateAndPlace(@Param("users") Users users,@Param("place") Place place ,@Param("date") Date date);

    Optional<Posting> findById(Long postingId);
}
