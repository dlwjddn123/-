package com.footstep.domain.posting.service;

import com.footstep.domain.posting.domain.place.Place;
import com.footstep.domain.posting.domain.posting.Posting;
import com.footstep.domain.posting.dto.AllPlaceDto;
import com.footstep.domain.posting.dto.SpecificPlaceDto;
import com.footstep.domain.posting.repository.PlaceRepository;
import com.footstep.domain.posting.repository.PostingRepository;
import com.footstep.domain.users.domain.Users;
import com.footstep.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PlacesService {

    private final PlaceRepository placeRepository;
    private final PostingRepository postingRepository;
    private final UsersRepository usersRepository;

    public SpecificPlaceDto viewSpecificPlace(Authentication authentication, @RequestBody Long placeId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Users currentUsers = usersRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NullPointerException("로그인이 필요합니다."));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NullPointerException("해당 장소가 없습니다."));
        List<Posting> postings = postingRepository.findByUsersAndPlaceOrderByCreatedDateDesc(currentUsers, place);
        return SpecificPlaceDto.builder()
                .name(place.getName())
                .imageUrl(postings.get(0).getImageUrl())
                .postingCount(postings.size())
                .build();
    }

    public AllPlaceDto viewAllPlace(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Users currentUsers = usersRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NullPointerException("로그인이 필요합니다."));
        List<Place> places = postingRepository.findDistinctByUsers(currentUsers);
        return new AllPlaceDto(places);
        /*
        List<AllPlaceDto> allPlaceDto = new ArrayList<>();
        for (Place place : places) {
            allPlaceDto.add(new AllPlaceDto(place.getId()));
        }
        return allPlaceDto;
         */
    }
}
