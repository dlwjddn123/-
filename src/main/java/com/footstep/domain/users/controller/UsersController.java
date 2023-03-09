package com.footstep.domain.users.controller;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.base.BaseResponse;
import com.footstep.domain.report.dto.CreateReportDto;
import com.footstep.domain.report.service.ReportService;
import com.footstep.domain.users.dto.JoinDto;
import com.footstep.domain.users.dto.changeProfileInfo.ChangeNicknameInfo;
import com.footstep.domain.users.dto.changeProfileInfo.ChangePasswordInfo;
import com.footstep.domain.users.dto.MyPageInfo;
import com.footstep.domain.users.dto.TokenDto;
import com.footstep.domain.users.service.UsersService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Api(tags = {"회원 정보 API"})
@ApiResponses({
        @ApiResponse(code = 500, message = "Internal Server Error")
})
@RestController
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;
    private final ReportService reportService;

    @ApiOperation(value = "회원 가입")
    @ApiResponses({
            @ApiResponse(code = 2016, message = "이메일 형식을 확인해주세요."),
            @ApiResponse(code = 2018, message = "비밀번호를 입력해주세요."),
            @ApiResponse(code = 2019, message = "닉네임을 입력해주세요."),
            @ApiResponse(code = 3011, message = "중복된 이메일입니다."),
            @ApiResponse(code = 3012, message = "이미 존재하는 닉네임입니다.")
    })
    @PostMapping("/join")
    public BaseResponse<String> join(@Valid @RequestBody JoinDto joinDto, BindingResult bindingResult) {
        try {
            if(bindingResult.hasErrors())
                usersService.isValid(bindingResult.getFieldErrors().get(0).getField());
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImZvb3RzdGVwQG5hdmVyLmNvbSIsImlhdCI6MTY3NjAwOTY1OSwiZXhwIjoxNjc2MzEyMDU5fQ.VBt8rfM3W7JdH5jMQ7A19-tuZ3OGLBqzmRC8GF2DzGQ")
    })
    @GetMapping("/my-page")
    public BaseResponse<MyPageInfo> getMyPage(@RequestHeader("Authorization")String accessToken) {
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
            @ApiResponse(code = 2020, message = "현재 비밀번호를 입력해주세요."),
            @ApiResponse(code = 2021, message = "변경할 비밀번호를 입력해주세요."),
            @ApiResponse(code = 3013, message = "현재 비밀번호와 변경할 비밀번호가 같습니다."),
            @ApiResponse(code = 3015, message = "비밀번호가 다릅니다.")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImZvb3RzdGVwQG5hdmVyLmNvbSIsImlhdCI6MTY3NjAwOTY1OSwiZXhwIjoxNjc2MzEyMDU5fQ.VBt8rfM3W7JdH5jMQ7A19-tuZ3OGLBqzmRC8GF2DzGQ")
    })
    @PatchMapping("/my-page/password")
    public BaseResponse<String> changePassword(@RequestHeader("Authorization")String accessToken, @Valid @RequestBody ChangePasswordInfo changePasswordInfo, BindingResult bindingResult) {
        try {
            if(bindingResult.hasErrors())
                usersService.isValid(bindingResult.getFieldErrors().get(0).getField());
            usersService.changePassword(changePasswordInfo);
            return new BaseResponse<>("비밀번호가 변경되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "유저 신고",
            notes = "유저 신고하기"
    )
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2060, message = "이미 신고한 컨텐츠(유저, 게시글 혹은 댓글) 입니다."),
            @ApiResponse(code = 3014, message = "없는 아이디입니다.")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImZvb3RzdGVwQG5hdmVyLmNvbSIsImlhdCI6MTY3NjAwOTY1OSwiZXhwIjoxNjc2MzEyMDU5fQ.VBt8rfM3W7JdH5jMQ7A19-tuZ3OGLBqzmRC8GF2DzGQ")
    })
    @PostMapping("/{users-id}/users-report")
    public BaseResponse<String> reportUsers(@ApiParam(value = "유저 ID", required = true, example = "1") @PathVariable("users-id")Long usersId,
                                              @RequestHeader("Authorization")String accessToken,
                                            @RequestBody CreateReportDto createReportDto) {
        try {
            reportService.createReport(createReportDto, usersId);
            return new BaseResponse<>("신고 성공!");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "닉네임 변경",
            notes = "이미 존재하는 닉네임인지 확인 후 닉네임 변경")
    @ApiResponses({
            @ApiResponse(code = 2005, message = "로그인이 필요합니다."),
            @ApiResponse(code = 2019, message = "닉네임을 입력해주세요."),
            @ApiResponse(code = 3012, message = "이미 존재하는 닉네임입니다.")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImZvb3RzdGVwQG5hdmVyLmNvbSIsImlhdCI6MTY3NjAwOTY1OSwiZXhwIjoxNjc2MzEyMDU5fQ.VBt8rfM3W7JdH5jMQ7A19-tuZ3OGLBqzmRC8GF2DzGQ")
    })
    @PatchMapping("/my-page/nickname")
    public BaseResponse<String> changeNickname(@RequestHeader("Authorization")String accessToken, @Valid @RequestBody ChangeNicknameInfo changeNicknameInfo, BindingResult bindingResult) {
        try {
            if(bindingResult.hasErrors())
                usersService.isValid(bindingResult.getFieldErrors().get(0).getField());
            usersService.changeNickname(changeNicknameInfo.getNickname());
            return new BaseResponse<>("닉네임이 변경되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(value = "프로필 이미지 변경")
    @ApiResponse(code = 2005, message = "로그인이 필요합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImZvb3RzdGVwQG5hdmVyLmNvbSIsImlhdCI6MTY3NjAwOTM4OSwiZXhwIjoxNjc2MzExNzg5fQ.FzTNXWHuzRelBB_3FM5c-p5E5lFNIbpiD-cOVo3XWbQ")
    })
    @PatchMapping("/my-page/profile")
    public BaseResponse<String> changeProfileImage(@RequestHeader("Authorization")String accessToken, @RequestPart MultipartFile profile) throws IOException {
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImZvb3RzdGVwQG5hdmVyLmNvbSIsImlhdCI6MTY3NjAwOTM4OSwiZXhwIjoxNjc2MzExNzg5fQ.FzTNXWHuzRelBB_3FM5c-p5E5lFNIbpiD-cOVo3XWbQ"),
            @ApiImplicitParam(name = "RefreshToken", value = "refreshToken", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImZvb3RzdGVwQG5hdmVyLmNvbSIsImlhdCI6MTY3NjAwOTY1OSwiZXhwIjoxNjc4NjAxNjU5fQ.0rwgpCgbX_MowMP4Tv-kklyZsJHmNORwTHScrMkrEic")
    })
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

