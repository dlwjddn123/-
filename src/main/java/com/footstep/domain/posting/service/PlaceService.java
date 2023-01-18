package com.footstep.domain.posting.service;

import com.footstep.domain.base.Status;
import com.footstep.domain.posting.domain.place.City;
import com.footstep.domain.posting.domain.place.Place;
import com.footstep.domain.posting.dto.CreatePlaceDto;
import com.footstep.domain.posting.repository.PlaceRepository;
import com.footstep.domain.posting.util.CityConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceService {

    private final PlaceRepository placeRepository;

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
}
