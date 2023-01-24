package com.footstep.domain.posting.controller;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.base.BaseResponse;
import com.footstep.domain.base.BaseResponseStatus;
import com.footstep.domain.posting.dto.*;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.footstep.domain.posting.service.PostingService;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@Api(tags = "발자취 게시물 API")
@ApiResponses({
        @ApiResponse(code = 500, message = "Internal Server Error"),
        @ApiResponse(code = 2005, message = "로그인이 필요합니다.")
})
@RestController
@RequiredArgsConstructor
@RequestMapping("/footstep")
@ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImZvb3RzdGVwQG5hdmVyLmNvbSIsImlhdCI6MTY3NDU2NzU2NCwiZXhwIjoxNjc0ODY5OTY0fQ.lobF3T2kLImKawBXnMjrNr5KCww9e74h5xLqblIFNtk")
})
public class PostingController {

    private final PostingService postingService;

    @ApiOperation(
            value = "발자취 생성",
            notes = "발자취(게시물) 생성")
    @PostMapping(value = "/write")
    public BaseResponse<BaseResponseStatus> uploadPosting(@RequestHeader("Authorization")String accessToken, @ModelAttribute CreatePostingDto createPostingDto, @RequestPart MultipartFile image) throws IOException {
        try {
            postingService.uploadPosting(image, createPostingDto);
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
    public BaseResponse<PostingListResponseDto> viewGallery(@RequestHeader("Authorization")String accessToken) {
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
            @ApiParam(value = "장소 ID", required = true, example = "2") @PathVariable("posting-id") Long posting_id,
            @RequestHeader("Authorization")String accessToken) {
        try {
            SpecificPosting result = postingService.viewSpecificPosting(posting_id);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
