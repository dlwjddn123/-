package com.footstep.domain.users.controller;

import com.footstep.domain.users.domain.Users;
import com.footstep.domain.users.dto.ChangePasswordInfo;
import com.footstep.domain.users.dto.MyPageInfo;
import com.footstep.domain.users.dto.TokenDto;
import com.footstep.domain.users.repository.UsersRepository;
import com.footstep.domain.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;
    private final UsersRepository usersRepository;

    @GetMapping("/my-page")
    public MyPageInfo getMyPage(Authentication authentication) {
        return usersService.getMyPage(authentication);
    }

    @PatchMapping("/my-page/password")
    public void changePassword(Authentication authentication, @RequestBody ChangePasswordInfo changePasswordInfo) {
        usersService.changePassword(authentication, changePasswordInfo);
    }


    @PatchMapping("/my-page/nickname")
    public void changeNickname (Authentication authentication, @RequestBody Map<String, String> nicknameMap) {
        usersService.changeNickname(authentication, nicknameMap.get("nickname"));
    }

    @PatchMapping("/my-page/profile")
    public void changeProfileImage(Authentication authentication, @RequestBody Map<String, String> imageUrlMap) {
        usersService.changeProfileImage(authentication, imageUrlMap.get("imageUrl"));
    }

    @PatchMapping("/my-page/secession")
    public void secession(@RequestHeader("Authorization") String accessToken,
                          @RequestHeader("RefreshToken") String refreshToken,
                          Authentication authentication) {
        usersService.secession(authentication, TokenDto.of(accessToken, refreshToken));
    }
}

