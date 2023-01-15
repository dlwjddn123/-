package com.footstep.domain.users.controller;

import com.footstep.domain.users.dto.changeProfileInfo.ChangeNicknameInfo;
import com.footstep.domain.users.dto.changeProfileInfo.ChangePasswordInfo;
import com.footstep.domain.users.dto.MyPageInfo;
import com.footstep.domain.users.dto.TokenDto;
import com.footstep.domain.users.dto.changeProfileInfo.ChangeProfileImageInfo;
import com.footstep.domain.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/my-page")
    public MyPageInfo getMyPage(Authentication authentication) {
        return usersService.getMyPage(authentication);
    }

    @PatchMapping("/my-page/password")
    public void changePassword(Authentication authentication, @RequestBody ChangePasswordInfo changePasswordInfo) {
        usersService.changePassword(authentication, changePasswordInfo);
    }


    @PatchMapping("/my-page/nickname")
    public void changeNickname(Authentication authentication, @RequestBody ChangeNicknameInfo changeNicknameInfo) {
        usersService.changeNickname(authentication, changeNicknameInfo.getNickname());
    }

    @PatchMapping("/my-page/profile")
    public void changeProfileImage(Authentication authentication, @RequestBody ChangeProfileImageInfo changeProfileImageInfo) {
        usersService.changeProfileImage(authentication, changeProfileImageInfo.getProfileImageUrl());
    }

    @PatchMapping("/my-page/secession")
    public void secession(@RequestHeader("Authorization") String accessToken,
                          @RequestHeader("RefreshToken") String refreshToken,
                          Authentication authentication) {
        usersService.secession(authentication, TokenDto.of(accessToken, refreshToken));
    }
}

