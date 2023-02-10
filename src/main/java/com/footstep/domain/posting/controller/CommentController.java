package com.footstep.domain.posting.controller;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.base.BaseResponse;
import com.footstep.domain.base.BaseResponseStatus;
import com.footstep.domain.posting.dto.CreateCommentDto;
import com.footstep.domain.posting.service.CommentService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/footstep")
@Api(tags = {"댓글 API"})
@ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImZvb3RzdGVwQG5hdmVyLmNvbSIsImlhdCI6MTY3NjAwOTY1OSwiZXhwIjoxNjc2MzEyMDU5fQ.VBt8rfM3W7JdH5jMQ7A19-tuZ3OGLBqzmRC8GF2DzGQ")
})
@ApiResponses({
        @ApiResponse(code = 500, message = "Internal Server Error"),
        @ApiResponse(code = 2005, message = "로그인이 필요합니다.")
})
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{posting-id}/comment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "posting-id", value = "게시물 아이디", required = true, example = "3")
    })
    @ApiResponses({
            @ApiResponse(code = 2050, message = "댓글 내용을 입력해주세요."),
            @ApiResponse(code = 3031, message = "게시글이 존재하지 않습니다"),
            @ApiResponse(code = 2003, message = "권한이 없는 유저의 접근입니다.")
    })
    @ApiOperation(value = "댓글 생성", notes = "해당 게시글에 댓글 달기", response = CreateCommentDto.class)
    public BaseResponse<BaseResponseStatus> addComment(@PathVariable("posting-id")Long postingId, @RequestHeader("Authorization")String accessToken,
                                                       @Valid @RequestBody CreateCommentDto createCommentDto, BindingResult bindingResult) {
        try {
            if(bindingResult.hasErrors())
                commentService.isValid(bindingResult.getFieldErrors().get(0).getField());
            commentService.addComment(createCommentDto.getContent(), postingId);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PatchMapping("/{comment-id}")
    @ApiImplicitParam(name = "comment-id", value = "삭제할 댓글 아이디", required = true, example = "4")
    @ApiResponse(code = 3041, message = "해당 댓글이 존재하지 않습니다")
    @ApiOperation(value = "댓글 삭제", notes = "해당 댓글 삭제")
    public BaseResponse<BaseResponseStatus> deleteComment(@PathVariable("comment-id") Long commentId, @RequestHeader("Authorization")String accessToken) {
        try {
            commentService.deleteComment(commentId);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());

        }
    }

    @GetMapping("/{posting-id}/comments/count")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "posting-id", value = "해당 게시물 아이디", required = true, example = "3"),
    })
    @ApiResponse(code = 3031, message = "게시글이 존재하지 않습니다.")
    @ApiOperation(value = "댓글 개수", notes = "해당 게시물에 댓글 개수 세기")
    public BaseResponse<String> countComment(@PathVariable("posting-id")Long postingId, @RequestHeader("Authorization")String accessToken) {
        try {
            String result = commentService.count(postingId);
            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());

        }
    }
}
