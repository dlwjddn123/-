package com.footstep.domain.users.dto.changeProfileInfo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeProfileImageInfo {

    @ApiModelProperty(notes = "프로필 이미지 URL", example = "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMjEyMjlfMjAg%2FMDAxNjcyMjc4OTcwMzMy.G8ED1jrg5cmDvUjEPo-0EcOoST37816BVGRxTkqMSwsg.HEVCV7zEGHaSWFqoO9lMj8i4txdqbN71E-oa9C4Ev5sg.JPEG.laufey%2FIMG_2031.JPEG&type=sc960_832")
    private String profileImageUrl;
}
