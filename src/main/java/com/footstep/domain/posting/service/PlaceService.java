package com.footstep.domain.posting.service;

import com.footstep.domain.base.Status;
import com.footstep.domain.posting.domain.place.City;
import com.footstep.domain.posting.domain.place.Place;
import com.footstep.domain.posting.domain.posting.Posting;
import com.footstep.domain.posting.dto.*;
import com.footstep.domain.posting.repository.PlaceRepository;
import com.footstep.domain.posting.repository.PostingRepository;
import com.footstep.domain.posting.util.CityConverter;
import com.footstep.domain.users.domain.Users;
import com.footstep.domain.users.repository.UsersRepository;
import com.footstep.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PostingRepository postingRepository;
    private final UsersRepository usersRepository;

    public Place createPlace(CreatePlaceDto createPlaceDto) {
        Place place = Place.builder()
                .name(createPlaceDto.getName())
                .address(createPlaceDto.getAddress())
                .latitude(createPlaceDto.getLatitude())
                .longitude(createPlaceDto.getLongitude())
                .city(City.get(CityConverter.getCityCode(createPlaceDto.getAddress())))
                .status(Status.NORMAL)
                .build();

        placeRepository.save(place);
        return place;
    }

    public SpecificPlaceDto viewSpecificPlace(Long placeId) {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new IllegalStateException("로그인을 해주세요"));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NullPointerException("해당 장소가 없습니다."));
        List<Posting> postings = postingRepository.findByUsersAndPlaceOrderByCreatedDateDesc(currentUsers, place);
        if (postings.isEmpty())
            throw new NullPointerException("해당 장소에 작성된 게시글이 없습니다.");
        return SpecificPlaceDto.builder()
                .name(place.getName())
                .imageUrl(postings.get(0).getImageUrl())
                .postingCount(postings.size())
                .build();
    }

    public PostingListResponseDto viewSpecificPlaceList(Long placeId) {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new IllegalStateException("로그인을 해주세요"));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NullPointerException("해당 장소가 없습니다."));
        List<Posting> postings = postingRepository.findByUsersAndPlaceOrderByCreatedDateDesc(currentUsers, place);
        if (postings.isEmpty())
            throw new NullPointerException("해당 장소에 작성된 게시글이 없습니다.");
        List<Date> dates = postings.stream().map(Posting::getRecordDate).toList();

        List<PostingListDto> postingListDto = new ArrayList<>();
        for (Posting posting : postings) {
            PostingListDto dto = PostingListDto.builder()
                    .placeName(place.getName())
                    .recordDate(posting.getRecordDate())
                    .imageUrl(posting.getImageUrl())
                    .title(posting.getTitle())
                    .likes((long) posting.getLikeList().size())
                    .postings((long) Collections.frequency(dates, posting.getRecordDate()))
                    .postingId(posting.getId())
                    .build();
            postingListDto.add(dto);
        }
        return new PostingListResponseDto(postingListDto, (long) new HashSet<>(dates).stream().toList().size());
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