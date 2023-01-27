package com.footstep.domain.users.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersInfo {

    @ApiModelProperty(example = "footstep@naver.com")
    private String email;
    @ApiModelProperty(example = "하마")
    private String username;
}