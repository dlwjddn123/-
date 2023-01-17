package com.footstep.domain.posting.controller;

import com.footstep.domain.posting.domain.place.Place;
import com.footstep.domain.posting.dto.CreatePlaceDto;
import com.footstep.domain.posting.repository.PlaceRepository;
import com.footstep.domain.posting.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/footstep")
public class PlaceController {

    private final PlaceService placeService;
    private final PlaceRepository placeRepository;

    @PostMapping("/select-place")
    public ResponseEntity createSelectedPlace(@RequestBody CreatePlaceDto createPlaceDto) {
        placeService.createPlace(createPlaceDto);
        return new ResponseEntity(HttpStatus.OK);
    }
}
