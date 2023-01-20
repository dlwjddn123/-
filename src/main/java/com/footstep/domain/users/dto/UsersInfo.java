package com.footstep.domain.users.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersInfo {

    @ApiModelProperty(example = "lee1234@naver.com")
    private String email;
    @ApiModelProperty(example = "newJeans")
    private String username;
}