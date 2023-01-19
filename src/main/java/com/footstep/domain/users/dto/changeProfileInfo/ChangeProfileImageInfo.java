package com.footstep.domain.users.dto.changeProfileInfo;

import io.swagger.annotations.ApiParam;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeProfileImageInfo {

    @ApiParam(value = "프로필 이미지 URL", required = true, example = "url")
    private String profileImageUrl;
}
