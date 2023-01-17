package com.footstep.domain.posting.service;

import com.footstep.domain.posting.domain.place.Place;
import com.footstep.domain.posting.domain.posting.Posting;
import com.footstep.domain.posting.dto.AllPlaceDto;
import com.footstep.domain.posting.dto.SpecificPlaceDto;
import com.footstep.domain.posting.repository.PlaceRepository;
import com.footstep.domain.posting.repository.PostingRepository;
import com.footstep.domain.users.domain.Users;
import com.footstep.domain.users.repository.UsersRepository;
import com.footstep.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PlacesService {

    private final PlaceRepository placeRepository;
    private final PostingRepository postingRepository;
    private final UsersRepository usersRepository;

    public SpecificPlaceDto viewSpecificPlace(@RequestBody Long placeId) {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new IllegalStateException("로그인을 해주세요"));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NullPointerException("해당 장소가 없습니다."));
        List<Posting> postings = postingRepository.findByUsersAndPlaceOrderByCreatedDateDesc(currentUsers, place);
        return SpecificPlaceDto.builder()
                .name(place.getName())
                .imageUrl(postings.get(0).getImageUrl())
                .postingCount(postings.size())
                .build();
    }

    public AllPlaceDto viewAllPlace() {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new IllegalStateException("로그인을 해주세요"));
        List<Posting> postings = postingRepository.findByUsers(currentUsers);
        List<Long> placeIds = new ArrayList<>();
        for (Posting posting : postings) {
            placeIds.add(posting.getPlace().getId());
        }
        return new AllPlaceDto(new HashSet<>(placeIds).stream().toList());
    }
}
