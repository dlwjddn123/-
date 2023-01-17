package com.footstep.domain.posting.controller;

import com.footstep.domain.posting.service.LikeService;
import com.footstep.domain.users.domain.Users;
import com.footstep.domain.users.repository.UsersRepository;
import com.footstep.global.config.security.util.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/footstep/{posting_id}")
public class LikeController {

    private final LikeService likeService;
    private final UsersRepository usersRepository;

    @PostMapping("/like")
    @ApiOperation(value = "좋아요 생성", notes = "해당 게시물에 좋아요를 누름")
    public ResponseEntity<String> addLike(@PathVariable Long postingId) {
        Users currentUser = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(()
                -> new IllegalStateException("로그인을 해주세요"));
        boolean result = false;
        if (Objects.nonNull(currentUser)) {
            result = likeService.addLike(currentUser, postingId);
        }

        if (result == true) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/like")
    @ApiOperation(value = "좋아요 취소", notes = "해당 게시물에 좋아요 취소하기")
    public ResponseEntity<String> cancelLike(@PathVariable Long postingId) {
        Users currentUser = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(()
                -> new IllegalStateException("로그인을 해주세요"));
        if (currentUser != null) {
            likeService.cancelLike(currentUser, postingId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/like")
    @ApiOperation(value = "좋아요 개수", notes = "해당 게시물에 좋아요 개수 세기")
    public ResponseEntity<List<String>> likeCount(@PathVariable Long postingId) {
        Users currentUser = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail()).orElseThrow(()
                -> new IllegalStateException("로그인을 해주세요"));
        List<String> result = likeService.count(postingId, currentUser);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}