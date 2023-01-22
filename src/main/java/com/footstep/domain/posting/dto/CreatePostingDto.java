package com.footstep.domain.posting.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostingDto {

    @ApiModelProperty(value = "발자취 제목", required = true, example = "제목예시")
    private String title;
    @ApiModelProperty(value = "발자취 내용", required = true, example = "내용예시")
    private String content;
    @ApiModelProperty(value = "발자취 게시일", required = true, example = "2022-11-20")
    private Date recordDate;
//    @ApiModelProperty(value = "발자취 사진", required = false)
//    private String imageUrl;
    private CreatePlaceDto createPlaceDto;
    @ApiModelProperty(value = "발자취 공개 여부", required = true, example = "0")
    private int visibilityStatusCode;
}
