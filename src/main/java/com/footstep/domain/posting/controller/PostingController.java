package com.footstep.domain.posting.controller;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.base.BaseResponse;
import com.footstep.domain.base.BaseResponseStatus;
import com.footstep.domain.posting.dto.*;
import com.footstep.domain.report.dto.CreateReportDto;
import com.footstep.domain.report.service.ReportService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.footstep.domain.posting.service.PostingService;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Date;

@Api(tags = "발자취 게시물 API")
@ApiResponses({
        @ApiResponse(code = 500, message = "Internal Server Error"),
        @ApiResponse(code = 2005, message = "로그인이 필요합니다.")
})
@RestController
@RequiredArgsConstructor
@RequestMapping("/footstep")
@ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImZvb3RzdGVwQG5hdmVyLmNvbSIsImlhdCI6MTY3NjAwOTY1OSwiZXhwIjoxNjc2MzEyMDU5fQ.VBt8rfM3W7JdH5jMQ7A19-tuZ3OGLBqzmRC8GF2DzGQ")
})
public class PostingController {

    private final PostingService postingService;
    private final ReportService reportService;

    @ApiOperation(
            value = "발자취 생성",
            notes = "발자취(게시물) 생성")
    @ApiResponses({
            @ApiResponse(code = 2030, message = "제목을 입력해주세요."),
            @ApiResponse(code = 2031, message = "내용을 입력해주세요."),
            @ApiResponse(code = 2032, message = "게시일은 현재 또는 과거이어야 합니다."),
            @ApiResponse(code = 2033, message = "공개 여부 값은 0 또는 1이어야 합니다."),
            @ApiResponse(code = 2040, message = "장소명을 입력해주세요."),
            @ApiResponse(code = 2041, message = "주소를 입력해주세요."),
            @ApiResponse(code = 2042, message = "위도의 범위는 -90°~90° 입니다."),
            @ApiResponse(code = 2043, message = "경도의 범위는 -180°~180° 입니다.")
    })
    @PostMapping(value = "/write")
    public BaseResponse<BaseResponseStatus> uploadPosting(@RequestHeader("Authorization")String accessToken, @RequestPart MultipartFile image,
                                                          @Valid @ModelAttribute CreatePostingDto createPostingDto, BindingResult bindingResult) throws IOException {
        try {
            if(bindingResult.hasErrors())
                postingService.isValid(bindingResult.getFieldErrors().get(0).getField());
            postingService.uploadPosting(image, createPostingDto);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "발자취 수정",
            notes = "발자취 수정 데이터 가져오기"
    )
    @GetMapping("/{posting-id}/edit")
    public BaseResponse<EditPostingDto> getPosting(@RequestHeader("Authorization")String accessToken,
                                                   @ApiParam(value = "게시물 ID", required = true, example = "3")
                                                   @PathVariable("posting-id")Long postingId) {
        try {
            EditPostingDto postingInfo = postingService.getPostingInfo(postingId);
            return new BaseResponse<>(postingInfo);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "발자취 수정",
            notes = "발자취 수정하기"
    )
    @ApiResponses({
            @ApiResponse(code = 2030, message = "제목을 입력해주세요."),
            @ApiResponse(code = 2031, message = "내용을 입력해주세요."),
            @ApiResponse(code = 2032, message = "게시일은 현재 또는 과거이어야 합니다."),
            @ApiResponse(code = 2033, message = "공개 여부 값은 0 또는 1이어야 합니다."),
            @ApiResponse(code = 2040, message = "장소명을 입력해주세요."),
            @ApiResponse(code = 2041, message = "주소를 입력해주세요."),
            @ApiResponse(code = 2042, message = "위도의 범위는 -90°~90° 입니다."),
            @ApiResponse(code = 2043, message = "경도의 범위는 -180°~180° 입니다.")
    })
    @PostMapping("/{posting-id}/edit")
    public BaseResponse<String> editPosting(@ApiParam(value = "게시물 ID", required = true, example = "3")
                                                    @PathVariable("posting-id")Long postingId,
                                                    @RequestHeader("Authorization")String accessToken,
                                                    @RequestPart MultipartFile image,
                                                    @Valid @ModelAttribute CreatePostingDto createPostingDto,
                                                    BindingResult bindingResult) throws IOException {
        try {
            if(bindingResult.hasErrors())
                postingService.isValid(bindingResult.getFieldErrors().get(0).getField());
            postingService.editPosting(postingId, image, createPostingDto);
            return new BaseResponse<>("수정 성공!");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "발자취 신고",
            notes = "발자취 신고하기"
    )
    @ApiResponses({
            @ApiResponse(code = 2060, message = "이미 신고한 컨텐츠(유저, 게시글 혹은 댓글) 입니다."),
            @ApiResponse(code = 3031, message = "게시글이 존재하지 않습니다.")
    })
    @PostMapping("/{posting-id}/posting-report")
    public BaseResponse<String> reportPosting(@ApiParam(value = "게시물 ID", required = true, example = "3") @PathVariable("posting-id")Long postingId,
                                              @RequestHeader("Authorization")String accessToken,
                                              @RequestBody CreateReportDto createReportDto) {
        try {
            reportService.createReport(createReportDto, postingId);
            return new BaseResponse<>("신고 성공!");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "발자취 삭제",
            notes = "발자취 삭제하기"
    )
    @PatchMapping("/{posting-id}/remove")
    public BaseResponse<String> removePosting(@ApiParam(value = "게시물 ID", required = true, example = "3") @PathVariable("posting-id")Long postingId,
                                              @RequestHeader("Authorization")String accessToken) {
        try {
            postingService.removePosting(postingId);
            return new BaseResponse<>("삭제 성공!");
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
            value = "갤러리 발자취 캘린더 지정 조회",
            notes = "현재 사용자가 지정한 날짜의 갤러리 발자취에 대해 리스트 형태로 조회",
            response = PostingListResponseDto.class)
    @ApiResponses({
            @ApiResponse(code = 3031, message = "게시글이 존재하지 않습니다.")
    })
    @GetMapping("/gallery/{date}")
    public BaseResponse<DesignatedPostingDto> viewDesignatedGallery(@RequestHeader("Authorization")String accessToken,
                                                                    @ApiParam(value = "날짜", required = true, example = "2023-01-23")
                                                                    @PathVariable Date date) {
        try {
            DesignatedPostingDto result = postingService.viewDesignatedGallery(date);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "발자취 게시물 상세조회",
            notes = "posting-id 넘어오면 해당 게시물 조회",
            response = SpecificPostingDto.class)
    @ApiResponses({
            @ApiResponse(code = 3021, message = "없는 장소입니다."),
            @ApiResponse(code = 3031, message = "게시글이 존재하지 않습니다.")
    })
    @ResponseBody
    @GetMapping("/posting/{posting-id}")
    public BaseResponse<SpecificPostingDto> specificPosting(
            @ApiParam(value = "게시물 ID", required = true, example = "3") @PathVariable("posting-id")Long postingId,
            @RequestHeader("Authorization")String accessToken) {
        try {
            SpecificPostingDto result = postingService.viewSpecificPosting(postingId);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "[특정 기간 조회] 지도에 발자취 표시",
            notes = "조회 기간 설정 후 조회된 발자취 리스트 조회",
            response = AllPlaceDto.class)
    @ApiResponses({
            @ApiResponse(code = 3031, message = "게시글이 존재하지 않습니다.")
    })
    @GetMapping("/specific/{start-date}/{end-date}")
    public BaseResponse<SpecificDateResponseDto> specificDatePosting(
            @ApiParam(value = "조회 시작 날짜", required = true, example = "2023-01-22") @PathVariable("start-date") Date startDate,
            @ApiParam(value = "조회 끝 날짜", required = true, example = "2023-01-24") @PathVariable("end-date") Date endDate,
            @RequestHeader("Authorization")String accessToken) {
        try {
            SpecificDateResponseDto result = postingService.viewSpecificDatePosting(startDate, endDate);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "유저 피드 리스트 조회",
            notes = "현재 사용자를 제외한 모든 유저에 대한 게시글을 리스트 형태로 조회",
            response = FeedListResponseDto.class)
    @ApiResponses({
            @ApiResponse(code = 3031, message = "게시글이 존재하지 않습니다.")
    })
    @GetMapping("/feed")
    public BaseResponse<FeedListResponseDto> viewFeed(@RequestHeader("Authorization")String accessToken) {
        try {
            FeedListResponseDto result = postingService.viewFeed();
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "특정 유저 피드 리스트 조회",
            notes = "특정 사용자가 생성한 발자취에 대해 리스트 형태로 조회",
            response = PostingListResponseDto.class)
    @ApiResponses({
            @ApiResponse(code = 2000, message = "입력값을 확인해주세요."),
            @ApiResponse(code = 3031, message = "게시글이 존재하지 않습니다.")
    })
    @GetMapping("/feed/{user-id}")
    public BaseResponse<PostingListResponseDto> viewSpecificFeedList(
            @ApiParam(value = "사용자 ID", required = true, example = "1") @PathVariable("user-id") Long userId,
            @RequestHeader("Authorization")String accessToken) {
        try {
            PostingListResponseDto result = postingService.viewSpecificFeedList(userId);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
