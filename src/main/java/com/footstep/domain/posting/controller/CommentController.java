package com.footstep.domain.posting.controller;

import com.footstep.domain.posting.dto.CreateCommentDto;
import com.footstep.domain.posting.service.CommentService;
import com.footstep.domain.users.domain.Users;
import com.footstep.domain.users.repository.UsersRepository;
import com.footstep.global.config.security.util.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/footstep/{posting_id}")
public class CommentController {

    private final CommentService commentService;
    private final UsersRepository usersRepository;

    @PostMapping("/comment")
    @ApiOperation(value = "댓글 생성", notes = "해당 게시글에 댓글 달기")
    public ResponseEntity<String> addComment( @PathVariable Long posting_id, @RequestBody CreateCommentDto createCommentDto) {
        Users currentUser = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(()
                -> new IllegalStateException("로그인을 해주세요"));
        boolean result = false;
        if (Objects.nonNull(currentUser)) {
            result = commentService.addComment(createCommentDto.getContent(), currentUser, posting_id);
        }
        if (result == true) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{comment_id}")
    @ApiOperation(value = "댓글 삭제", notes = "해당 댓글 삭제")
    public ResponseEntity<String> deleteComment(@PathVariable Long comment_id) {
        Users currentUser = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(()
                -> new IllegalStateException("로그인을 해주세요"));
        commentService.deleteComment(comment_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/comments/count")
    @ApiOperation(value = "댓글 개수", notes = "해당 게시물에 댓글 개수 세기")
    public ResponseEntity<String> countComment(@PathVariable Long posting_id) {
        Users currentUser = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(()
                -> new IllegalStateException("로그인을 해주세요"));
        String result = commentService.count(posting_id, currentUser);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
