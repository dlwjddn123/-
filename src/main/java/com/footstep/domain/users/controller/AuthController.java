package com.footstep.domain.users.controller;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.base.BaseResponse;
import com.footstep.domain.users.dto.LoginDto;
import com.footstep.domain.users.dto.TokenDto;
import com.footstep.domain.users.service.AuthService;
import com.footstep.global.config.jwt.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"회원 인증 API"})
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenUtil jwtTokenUtil;

    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public BaseResponse<TokenDto> login(@RequestBody LoginDto loginDto) {
        try {
            return new BaseResponse<>(authService.login(loginDto));
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PostMapping("/reissue")
    public BaseResponse<TokenDto> reissue(@RequestHeader("RefreshToken") String refreshToken) {
        try {
            return new BaseResponse<>(authService.reissue(refreshToken));
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

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
