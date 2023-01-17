package com.footstep.domain.posting.controller;

import com.footstep.domain.posting.dto.AllPlaceDto;
import com.footstep.domain.posting.dto.SpecificPlaceDto;
import com.footstep.domain.posting.service.PlacesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostingController {

    private final PlacesService placesService;

    @GetMapping("/footstep/{place_id}")
    public SpecificPlaceDto viewSpecificPlace(Authentication authentication, @PathVariable("place_id") Long place_id) {
        return placesService.viewSpecificPlace(authentication, place_id);
    }

    @GetMapping("/footstep/all")
    public AllPlaceDto viewAllPlace(Authentication authentication) {
        return placesService.viewAllPlace(authentication);
    }
}
