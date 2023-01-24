package com.footstep.domain.posting.controller;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.base.BaseResponse;
import com.footstep.domain.posting.service.LikeService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"좋아요 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/footstep/{posting_id}")
@ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImxlZTEyMzQ1QG5hdmVyLmNvbSIsImlhdCI6MTY3NDUyMzk4MywiZXhwIjoxNjc0ODI2MzgzfQ.aq8EcJLI-oyI-Qs4vF_SyVP0B6a0C4CXDU624bNSQRg")
})
public class LikeController {

    private final LikeService likeService;

    @ApiResponses({
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 2005, message = "로그인이 필요합니다.")
    })
    @PostMapping("/like")
    @ApiResponse(code = 3031, message = "게시글이 존재하지 않습니다")
    @ApiOperation(value = "좋아요 누르기", notes = "해당 게시물에 좋아요를 누름, 눌려있는 상태에서 좋아요를 누르면 취소")
    public BaseResponse<String> like(
            @ApiParam(value = "게시물 ID", required = true, example = "1") @PathVariable Long posting_id,
            @RequestHeader("Authorization")String accessToken) {
        try {
            String result = likeService.like(posting_id);
            return new BaseResponse<>(result);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

//    @DeleteMapping("/like")
//    @ApiResponses({
//            @ApiResponse(code = 3031, message = "게시글이 존재하지 않습니다."),
//            @ApiResponse(code = 3051, message = "해당 좋아요가 존재하지 않습니다")
//    })
//    @ApiOperation(value = "좋아요 취소", notes = "해당 게시물에 좋아요 취소하기")
//    public BaseResponse<String> cancelLike(
//            @ApiParam(value = "게시물 ID", required = true, example = "1") @PathVariable Long posting_id) {
//        try {
//            likeService.cancelLike(posting_id);
//            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
//        }catch (BaseException exception){
//            return new BaseResponse<>(exception.getStatus());
//        }
//    }

    @GetMapping("/like")
    @ApiResponse(code = 3031, message = "게시글이 존재하지 않습니다.")
    @ApiOperation(value = "좋아요 개수", notes = "해당 게시물에 좋아요 개수 세기")
    public BaseResponse<String> countLike(
            @ApiParam(value = "게시물 ID", required = true, example = "1") @PathVariable Long posting_id,
            @RequestHeader("Authorization")String accessToken) {
        try {
            String result = likeService.count(posting_id);
            return new BaseResponse<>(result);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}