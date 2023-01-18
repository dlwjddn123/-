package com.footstep.domain.posting.controller;

import com.footstep.domain.posting.dto.CreatePostingDto;
import com.footstep.domain.posting.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/footstep")
public class PostingController {

    private final PostingService postingService;

    @PostMapping("/write")
    public ResponseEntity uploadPosting(@RequestBody CreatePostingDto createPostingDto) {
        postingService.uploadPosting(createPostingDto);
        return new ResponseEntity(HttpStatus.OK);
    }
}
