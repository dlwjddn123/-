package com.footstep.domain.posting.controller;

import com.footstep.domain.posting.dto.*;
import com.footstep.domain.posting.service.PlaceService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.footstep.domain.posting.service.PostingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

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

    @GetMapping("/{place_id}/list")
    public PostingListResponseDto viewSpecificPlaceList(@PathVariable("place_id") Long place_id) {
        return placeService.viewSpecificPlaceList(place_id);
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
    @ResponseBody
    @GetMapping("/posting/{posting-id}")
    @ApiOperation(value = "발자취 게시물 상세조회", notes = "posting-id 넘어오면 해당 게시물 조회")
    public ResponseEntity<SpecificPosting> specificPosting(@PathVariable("posting-id") Long posting_id) {
        SpecificPosting result = postingService.viewSpecificPosting(posting_id);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
}
