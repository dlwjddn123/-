package com.footstep.domain.users.controller;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.base.BaseResponse;
import com.footstep.domain.users.dto.LoginDto;
import com.footstep.domain.users.dto.TokenDto;
import com.footstep.domain.users.service.AuthService;
import com.footstep.global.config.jwt.JwtTokenUtil;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"회원 인증 API"})
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenUtil jwtTokenUtil;

    @ApiOperation(
            value = "로그인",
            notes = "이메일과 비밀번호를 입력하여 로그인")
    @ApiResponses({
            @ApiResponse(code = 3014, message = "없는 아이디입니다."),
            @ApiResponse(code = 3015, message = "비밀번호가 다릅니다."),
            @ApiResponse(code = 3016, message = "탈퇴한 회원입니다."),
    })
    @PostMapping("/login")
    public BaseResponse<TokenDto> login(@RequestBody LoginDto loginDto) {
        try {
            return new BaseResponse<>(authService.login(loginDto));
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ApiOperation(
            value = "토큰 재발급",
            notes = "로그인 유지를 위한 AccessToken 재발급")
    @ApiResponses({
            @ApiResponse(code = 2001, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 2004, message = "토큰이 일치하지 않습니다.")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "RefreshToken", value = "refreshToken", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImZvb3RzdGVwQG5hdmVyLmNvbSIsImlhdCI6MTY3NDY2MDA5MSwiZXhwIjoxNjc0OTYyNDkxfQ.W7MNMFI43SPbcw5pLhpbsuic0_nCDRcqHKPgEipV9ko")
    })
    @PostMapping("/reissue")
    public BaseResponse<TokenDto> reissue(@RequestHeader("RefreshToken") String refreshToken) {
        try {
            return new BaseResponse<>(authService.reissue(refreshToken));
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    @ApiOperation(
            value = "로그아웃",
            notes = "현재 로그인 되어있는 유저를 로그아웃")
    @ApiResponses({
            @ApiResponse(code = 2001, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 2004, message = "토큰이 일치하지 않습니다."),
            @ApiResponse(code = 2006, message = "잘못된 접근입니다.")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "accessToken", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImZvb3RzdGVwQG5hdmVyLmNvbSIsImlhdCI6MTY3NDY2MDA5MSwiZXhwIjoxNjc0OTYyNDkxfQ.W7MNMFI43SPbcw5pLhpbsuic0_nCDRcqHKPgEipV9ko"),
            @ApiImplicitParam(name = "RefreshToken", value = "refreshToken", required = true, example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImZvb3RzdGVwQG5hdmVyLmNvbSIsImlhdCI6MTY3NDU2NzU2NCwiZXhwIjoxNjc3MTU5NTY0fQ.zhn4mePceDlROk3vJFUOQfhHxtsmS1vsthsBIEinSIk")
    })
    @PostMapping("/logout")
    public BaseResponse<String> logout(@RequestHeader("Authorization") String accessToken,
                       @RequestHeader("RefreshToken") String refreshToken) {
        String username = jwtTokenUtil.getUsername(resolveToken(accessToken));
        try {
            authService.logout(TokenDto.of(accessToken, refreshToken), username);
            return new BaseResponse<>("로그아웃 되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    private String resolveToken(String accessToken) {
        return accessToken.substring(7);
    }
}
