package com.footstep.domain.users.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto {

    @ApiModelProperty(example = "footstep@naver.com")
    private String email;

    @ApiModelProperty(example = "footstep12")
    private String password;
}
