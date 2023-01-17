package com.footstep.domain.posting.service;

import com.footstep.domain.posting.domain.place.Place;
import com.footstep.domain.posting.domain.posting.Posting;
import com.footstep.domain.posting.dto.AllPlaceDto;
import com.footstep.domain.posting.dto.SpecificPlaceDto;
import com.footstep.domain.posting.dto.SpecificPlaceListDto;
import com.footstep.domain.posting.repository.PlaceRepository;
import com.footstep.domain.posting.repository.PostingRepository;
import com.footstep.domain.users.domain.Users;
import com.footstep.domain.users.repository.UsersRepository;
import com.footstep.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PlacesService {

    private final PlaceRepository placeRepository;
    private final PostingRepository postingRepository;
    private final UsersRepository usersRepository;

    public SpecificPlaceDto viewSpecificPlace(Long placeId) {
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

    public List<SpecificPlaceListDto> viewSpecificPlaceList(Long placeId) {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new IllegalStateException("로그인을 해주세요"));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NullPointerException("해당 장소가 없습니다."));
        List<Posting> postings = postingRepository.findByUsersAndPlaceOrderByCreatedDateDesc(currentUsers, place);
        List<Date> dates = postings.stream().map(Posting::getRecordDate).toList();

        List<SpecificPlaceListDto> specificPlaceListDto = new ArrayList<>();
        for (Posting posting : postings) {
            SpecificPlaceListDto dto = SpecificPlaceListDto.builder()
                    .placeName(place.getName())
                    .recordDate(posting.getRecordDate())
                    .imageUrl(posting.getImageUrl())
                    .title(posting.getTitle())
                    .likes((long) posting.getLikeList().size())
                    .postings((long) Collections.frequency(dates, posting.getRecordDate()))
                    .postingId(posting.getId())
                    .build();
            specificPlaceListDto.add(dto);
        }
        return specificPlaceListDto;
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
