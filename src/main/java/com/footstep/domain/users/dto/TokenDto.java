package com.footstep.domain.users.dto;

import com.footstep.global.config.jwt.constants.JwtHeaderUtil;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {

    @ApiModelProperty(example = "Bearer ")
    private String grantType;
    @ApiModelProperty(example = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImxlZTEyMzRAbmF2ZXIuY29tIiwiaWF0IjoxNjc0MTc2ODEzLCJleHAiOjE2NzQ0NzkyMTN9.6IMBXjXFwc7Aj8tkDIGK2szBhTQrgnNate4WOxIxB8c!")
    private String accessToken;
    @ApiModelProperty(example = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImxlZTEyMzRAbmF2ZXIuY29tIiwiaWF0IjoxNjc0MTc2ODEzLCJleHAiOjE2NzY3Njg4MTN9.8iOD6Cr0j3ZHfCN9VE0JTiXV3DsqOD3cPYIhPRjBUJw")
    private String refreshToken;

    public static TokenDto of(String accessToken, String refreshToken) {
        return TokenDto.builder()
                .grantType(JwtHeaderUtil.GRANT_TYPE.getValue())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}