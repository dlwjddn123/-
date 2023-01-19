package com.footstep.domain.posting.controller;

import com.footstep.domain.posting.dto.*;
import com.footstep.domain.posting.service.PlaceService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.footstep.domain.posting.service.PostingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.List;

@ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
@RestController
@RequiredArgsConstructor
@RequestMapping("/footstep")
public class PostingController {

    private final PlaceService placeService;
    private final PostingService postingService;

    @ApiOperation(
            value = "특정 위치의 발자취 장소(팝업) 조회",
            notes = "특정 위치 ID를 이용해 현재 사용자가 해당 위치에 생성한 발자취에 대한 간략한 정보 조회",
            response = SpecificPlaceDto.class)
    @GetMapping("/{place_id}")
    public SpecificPlaceDto viewSpecificPlace(
            @ApiParam(value = "장소 ID", required = true, example = "1") @PathVariable("place_id") Long place_id) {
        return placeService.viewSpecificPlace(place_id);
    }

    @ApiOperation(
            value = "특정 위치의 발자취 장소(팝업) 클릭 후 게시물 리스트 조회",
            notes = "특정 위치 ID를 이용해 현재 사용자가 해당 위치에 생성한 발자취에 대해 리스트 형태로 조회",
            response = PostingListResponseDto.class)
    @GetMapping("/{place_id}/list")
    public PostingListResponseDto viewSpecificPlaceList(
            @ApiParam(value = "장소 ID", required = true, example = "1") @PathVariable("place_id") Long place_id) {
        return placeService.viewSpecificPlaceList(place_id);
    }

    @ApiOperation(
            value = "모든 발자취 조회",
            notes = "사용자가 생성한 모든 발자취의 위치 정보 조회",
            response = AllPlaceDto.class)
    @GetMapping("/all")
    public AllPlaceDto viewAllPlace() {
        return placeService.viewAllPlace();
    }

    @ApiOperation(
            value = "발자취 생성",
            notes = "발자취(게시물) 생성",
            response = ResponseEntity.class)
    @ApiImplicitParams(
            @ApiImplicitParam(name = "AccessToken", value = "asdasdsad", required = true, dataTypeClass = String.class)
    )
    @PostMapping("/write")
    public ResponseEntity uploadPosting(@RequestBody CreatePostingDto createPostingDto) {
        postingService.uploadPosting(createPostingDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(
            value = "갤러리 발자취 조회",
            notes = "현재 사용자가 생성한 발자취에 대해 리스트 형태로 조회",
            response = PostingListResponseDto.class)
    @GetMapping("/gallery")
    public ResponseEntity<PostingListResponseDto> viewGallery() {
        PostingListResponseDto result = postingService.viewGallery();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(
            value = "발자취 게시물 상세조회",
            notes = "posting-id 넘어오면 해당 게시물 조회",
            response = SpecificPosting.class)
    @ResponseBody
    @GetMapping("/posting/{posting-id}")
    public ResponseEntity<SpecificPosting> specificPosting(
            @ApiParam(value = "장소 ID", required = true, example = "1") @PathVariable("posting-id") Long posting_id) {
        SpecificPosting result = postingService.viewSpecificPosting(posting_id);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
}
