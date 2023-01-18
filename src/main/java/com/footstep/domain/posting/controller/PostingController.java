package com.footstep.domain.posting.controller;

import com.footstep.domain.posting.dto.AllPlaceDto;
import com.footstep.domain.posting.dto.PostingListResponseDto;
import com.footstep.domain.posting.dto.SpecificPlaceDto;
import com.footstep.domain.posting.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.footstep.domain.posting.dto.CreatePostingDto;
import com.footstep.domain.posting.service.PostingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@RestController
@RequiredArgsConstructor
@RequestMapping("/footstep")
public class PostingController {

    private final PlaceService placeService;
    private final PostingService postingService;

    @GetMapping("/{place_id}")
    public SpecificPlaceDto viewSpecificPlace(@PathVariable("place_id") Long place_id) {
        return placeService.viewSpecificPlace(place_id);
    }

    @GetMapping("/all")
    public AllPlaceDto viewAllPlace() {
        return placeService.viewAllPlace();
    }

    @PostMapping("/write")
    public ResponseEntity uploadPosting(@RequestBody CreatePostingDto createPostingDto) {
        postingService.uploadPosting(createPostingDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/gallery")
    public ResponseEntity<PostingListResponseDto> viewGallery() {
        PostingListResponseDto result = postingService.viewGallery();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
