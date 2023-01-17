package com.footstep.domain.posting.service;


import com.footstep.domain.posting.repository.LikeRepository;
import com.footstep.domain.posting.repository.PlaceRepository;
import com.footstep.domain.posting.repository.PostingRepository;
import com.footstep.domain.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class GalleryService {
    private final PostingRepository postingRepository;
    private final LikeRepository likeRepository;
    private final PlaceRepository placeRepository;


    public int countDate(Users users){
        Integer countDate =
    }

}
