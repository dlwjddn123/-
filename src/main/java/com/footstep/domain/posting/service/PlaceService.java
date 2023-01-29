package com.footstep.domain.posting.service;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.base.Status;
import static com.footstep.domain.base.BaseResponseStatus.*;
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

import java.sql.Date;
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

    public Optional<Place> getPlace(CreatePlaceDto createPlaceDto) {
        return placeRepository
                .findByLatitudeAndLongitude(createPlaceDto.getLatitude(), createPlaceDto.getLongitude());
    }

    public SpecificPlaceDto viewSpecificPlace(Long placeId) throws BaseException {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_PLACE));
        List<Posting> postings = postingRepository.findByUsersAndPlaceOrderByRecordDateDesc(currentUsers, place);
        if (postings.isEmpty())
            throw new BaseException(NOT_FOUND_POSTING);
        return SpecificPlaceDto.builder()
                .name(place.getName())
                .imageUrl(postings.get(0).getImageUrl())
                .postingCount(postings.size())
                .build();
    }

    public PostingListResponseDto viewSpecificPlaceList(Long placeId) throws BaseException {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_PLACE));
        List<Posting> postings = postingRepository.findByUsersAndPlaceOrderByRecordDateDesc(currentUsers, place);
        if (postings.isEmpty())
            throw new BaseException(NOT_FOUND_POSTING);
        List<Date> dates = postings.stream().map(Posting::getRecordDate).toList();

        List<PostingListDto> postingListDto = new ArrayList<>();
        for (Posting posting : postings) {
            PostingListDto dto = PostingListDto.builder()
                    .placeName(place.getName())
                    .recordDate(posting.getRecordDate())
                    .imageUrl(posting.getImageUrl())
                    .title(posting.getTitle())
                    .likes((long) posting.getLikeList().size())
                    .postingCount((long) Collections.frequency(dates, posting.getRecordDate()))
                    .postingId(posting.getId())
                    .build();
            postingListDto.add(dto);
        }
        return new PostingListResponseDto(postingListDto, (long) new HashSet<>(dates).stream().toList().size());
    }

    public PlaceLocationDto viewPlaceLocation(Double latitude, Double longitude) throws BaseException {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        CreatePlaceDto placeDto = CreatePlaceDto.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
        Place place = getPlace(placeDto)
                .orElseThrow(() -> new BaseException(NOT_FOUND_PLACE));
        List<Posting> postings = postingRepository.findByUsersAndPlaceOrderByRecordDateDesc(currentUsers, place);
        if (postings.isEmpty())
            throw new BaseException(NOT_FOUND_POSTING);
        return PlaceLocationDto.builder()
                .placeId(place.getId())
                .name(place.getName())
                .imageUrl(postings.get(0).getImageUrl())
                .postingCount(postings.size())
                .build();
    }

    public List<AllPlaceDto> viewAllPlace() throws BaseException {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        List<Posting> postings = postingRepository.findByUsers(currentUsers);
        List<AllPlaceDto> allPlaceDto = new ArrayList<>();
        for (Posting posting : postings) {
            AllPlaceDto dto = AllPlaceDto.builder()
                    .placeId(posting.getPlace().getId())
                    .placeName(posting.getPlace().getName())
                    .latitude(posting.getPlace().getLatitude())
                    .longitude(posting.getPlace().getLongitude())
                    .build();
            allPlaceDto.add(dto);
        }
        return allPlaceDto;
    }

    public DesignatedPostingDto viewSpecificPlaceDateList(Long placeId, Date date) throws BaseException {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_PLACE));
        List<Posting> postings = postingRepository.findByUsersAndRecordDateAndPlace(currentUsers, place, date);
        if (postings.isEmpty())
            throw new BaseException(NOT_FOUND_POSTING);
        List<Date> dates = postings.stream().map(Posting::getRecordDate).toList();
        List<PostingListDto> postingListDto = new ArrayList<>();

        for (Posting posting : postings) {
            PostingListDto dto = PostingListDto.builder()
                    .placeName(place.getName())
                    .recordDate(posting.getRecordDate())
                    .imageUrl(posting.getImageUrl())
                    .title(posting.getTitle())
                    .likes((long) posting.getLikeList().size())
                    .postingCount((long) Collections.frequency(dates, date))
                    .postingId(posting.getId())
                    .build();
            postingListDto.add(dto);
        }
        return new DesignatedPostingDto(postingListDto);
    }
}