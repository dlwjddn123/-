package com.footstep.domain.posting.controller;

import com.footstep.domain.posting.dto.AllPlaceDto;
import com.footstep.domain.posting.dto.SpecificPlaceDto;
import com.footstep.domain.posting.service.PlacesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.footstep.domain.posting.dto.CreatePostingDto;
import com.footstep.domain.posting.service.PostingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/footstep")
public class PostingController {

    private final PlacesService placesService;
    private final PostingService postingService;

    @GetMapping("/{place_id}")
    public SpecificPlaceDto viewSpecificPlace(@PathVariable("place_id") Long place_id) {
        return placesService.viewSpecificPlace(place_id);
    }

    @GetMapping("/all")
    public AllPlaceDto viewAllPlace() {
        return placesService.viewAllPlace();
    }

    @PostMapping("/write")
    public ResponseEntity uploadPosting(@RequestBody CreatePostingDto createPostingDto) {
        postingService.uploadPosting(createPostingDto);
        return new ResponseEntity(HttpStatus.OK);
    }
}
