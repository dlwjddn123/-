package com.footstep.domain.posting.controller;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.base.BaseResponse;
import com.footstep.domain.base.BaseResponseStatus;
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

@Api(tags = "발자취 게시물 API")
@ApiResponses({
        @ApiResponse(code = 500, message = "Internal Server Error"),
        @ApiResponse(code = 2005, message = "로그인이 필요합니다.")
})
@RestController
@RequiredArgsConstructor
@RequestMapping("/footstep")
public class PostingController {

    private final PostingService postingService;

    @ApiOperation(
            value = "발자취 생성",
            notes = "발자취(게시물) 생성",
            response = ResponseEntity.class)
    @ApiImplicitParams(
            @ApiImplicitParam(name = "AccessToken", value = "asdasdsad", required = true, dataTypeClass = String.class)
    )
    @PostMapping("/write")
    public BaseResponse<BaseResponseStatus> uploadPosting(@RequestBody CreatePostingDto createPostingDto) {
        try {
            postingService.uploadPosting(createPostingDto);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "갤러리 발자취 조회",
            notes = "현재 사용자가 생성한 발자취에 대해 리스트 형태로 조회",
            response = PostingListResponseDto.class)
    @ApiResponses({
            @ApiResponse(code = 3031, message = "게시글이 존재하지 않습니다.")
    })
    @GetMapping("/gallery")
    public BaseResponse<PostingListResponseDto> viewGallery() {
        try {
            PostingListResponseDto result = postingService.viewGallery();
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "발자취 게시물 상세조회",
            notes = "posting-id 넘어오면 해당 게시물 조회",
            response = SpecificPosting.class)
    @ApiResponses({
            @ApiResponse(code = 3021, message = "없는 장소입니다."),
            @ApiResponse(code = 3031, message = "게시글이 존재하지 않습니다.")
    })
    @ResponseBody
    @GetMapping("/posting/{posting-id}")
    public BaseResponse<SpecificPosting> specificPosting(
            @ApiParam(value = "장소 ID", required = true, example = "1") @PathVariable("posting-id") Long posting_id) {
        try {
            SpecificPosting result = postingService.viewSpecificPosting(posting_id);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }
}
