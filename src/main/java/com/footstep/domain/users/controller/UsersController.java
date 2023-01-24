package com.footstep.domain.users.controller;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.base.BaseResponse;
import com.footstep.domain.posting.dto.SpecificPlaceDto;
import com.footstep.domain.users.dto.JoinDto;
import com.footstep.domain.users.dto.changeProfileInfo.ChangeNicknameInfo;
import com.footstep.domain.users.dto.changeProfileInfo.ChangePasswordInfo;
import com.footstep.domain.users.dto.MyPageInfo;
import com.footstep.domain.users.dto.TokenDto;
import com.footstep.domain.users.service.UsersService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(tags = {"회원 정보 API"})
@ApiResponses({
        @ApiResponse(code = 500, message = "Internal Server Error")
})
@RestController
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @ApiOperation(value = "회원 가입")
    @ApiResponses({
            @ApiResponse(code = 3013, message = "중복된 이메일입니다."),
            @ApiResponse(code = 3017, message = "이미 존재하는 닉네임입니다.")
    })
    @PostMapping("/join")
    public BaseResponse<String> join(@RequestBody JoinDto joinDto) {
        try {
            usersService.join(joinDto);
            return new BaseResponse<>("회원 가입이 완료되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "마이 페이지 조회",
            notes = "현재 로그인한 유저의 마이 페이지를 조회",
            response = MyPageInfo.class)
    @ApiResponse(code = 2005, message = "로그인이 필요합니다.")
    @GetMapping("/my-page")
    public BaseResponse<MyPageInfo> getMyPage() {
        try {
            MyPageInfo myPage = usersService.getMyPage();
            return new BaseResponse<>(myPage);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "비밀번호 변경",
            notes = "현재 비밀번호를 확인하고 변경할 비밀번호를 입력하여 변경")
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 3015, message = "비밀번호가 다릅니다.")})
    @PatchMapping("/my-page/password")
    public BaseResponse<String> changePassword(@RequestBody ChangePasswordInfo changePasswordInfo) {
        try {
            usersService.changePassword(changePasswordInfo);
            return new BaseResponse<>("비밀번호가 변경되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "닉네임 변경",
            notes = "이미 존재하는 닉네임인지 확인 후 닉네임 변경")
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 3013, message = "이미 존재하는 닉네임입니다.")})
    @PatchMapping("/my-page/nickname")
    public BaseResponse<String> changeNickname(@RequestBody ChangeNicknameInfo changeNicknameInfo) {
        try {
            usersService.changeNickname(changeNicknameInfo.getNickname());
            return new BaseResponse<>("닉네임이 변경되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(value = "프로필 이미지 변경")
    @ApiResponse(code = 2005, message = "로그인이 필요합니다.")
    @PatchMapping("/my-page/profile")
    public BaseResponse<String> changeProfileImage(@RequestPart MultipartFile profile) throws IOException {
        try {
            usersService.changeProfileImage(profile);
            return new BaseResponse<>("프로필 이미지가 변경되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(value = "회원 탈퇴")
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2006, message = "잘못된 접근입니다.")})
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

