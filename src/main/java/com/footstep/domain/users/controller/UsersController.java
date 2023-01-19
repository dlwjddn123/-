package com.footstep.domain.users.controller;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.base.BaseResponse;
import com.footstep.domain.users.dto.JoinDto;
import com.footstep.domain.users.dto.changeProfileInfo.ChangeNicknameInfo;
import com.footstep.domain.users.dto.changeProfileInfo.ChangePasswordInfo;
import com.footstep.domain.users.dto.MyPageInfo;
import com.footstep.domain.users.dto.TokenDto;
import com.footstep.domain.users.dto.changeProfileInfo.ChangeProfileImageInfo;
import com.footstep.domain.users.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"회원 정보 API"})
@RestController
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @ApiOperation(value = "회원 가입")
    @PostMapping("/join")
    public BaseResponse<String> join(@RequestBody JoinDto joinDto) {
        try {
            usersService.join(joinDto);
            return new BaseResponse<>("회원 가입이 완료되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImxlZTEyMzRAbmF2ZXIuY29tIiwiaWF0IjoxNjc0MDU2NDY4LCJleHAiOjE2NzQzNTg4Njh9.I_L5A5RN8sklGQbXywHq8vjRraSyYk_-4_8pFGN6FoA", example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImxlZTEyMzRAbmF2ZXIuY29tIiwiaWF0IjoxNjc0MDU2NDY4LCJleHAiOjE2NzQzNTg4Njh9.I_L5A5RN8sklGQbXywHq8vjRraSyYk_-4_8pFGN6FoA"),
            @ApiImplicitParam(name = "RefreshToken", value = "refreshToken", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImxlZTEyMzRAbmF2ZXIuY29tIiwiaWF0IjoxNjc0MDU2NDY4LCJleHAiOjE2NzY2NDg0Njh9.bbDho3RzfxZpFPc5X7UEPmalca5CBxCZnh_DLmO0GuI", example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImxlZTEyMzRAbmF2ZXIuY29tIiwiaWF0IjoxNjc0MDU2NDY4LCJleHAiOjE2NzY2NDg0Njh9.bbDho3RzfxZpFPc5X7UEPmalca5CBxCZnh_DLmO0GuI")})
    @GetMapping("/my-page")
    public BaseResponse<MyPageInfo> getMyPage() {
        try {
            MyPageInfo myPage = usersService.getMyPage();
            return new BaseResponse<>(myPage);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(value = "비밀번호 변경")
    @ApiResponse(responseCode = "2000", description = "응깃..")
    @PatchMapping("/my-page/password")
    public BaseResponse<String> changePassword(@RequestBody ChangePasswordInfo changePasswordInfo) {
        try {
            usersService.changePassword(changePasswordInfo);
            return new BaseResponse<>("비밀번호가 변경되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PatchMapping("/my-page/nickname")
    public BaseResponse<String> changeNickname(@RequestBody ChangeNicknameInfo changeNicknameInfo) {
        try {
            usersService.changeNickname(changeNicknameInfo.getNickname());
            return new BaseResponse<>("닉네임이 변경되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    @PatchMapping("/my-page/profile")
    public BaseResponse<String> changeProfileImage(@RequestBody ChangeProfileImageInfo changeProfileImageInfo) {
        try {
            usersService.changeProfileImage(changeProfileImageInfo.getProfileImageUrl());
            return new BaseResponse<>("프로필 이미지가 변경되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PatchMapping("/my-page/secession")
    public BaseResponse<String> secession(@RequestHeader("Authorization") String accessToken,
                                          @RequestHeader("RefreshToken") String refreshToken) {
        try {
            usersService.secession(TokenDto.of(accessToken, refreshToken));
            return new BaseResponse<>("회원 탈퇴가 완료되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}

