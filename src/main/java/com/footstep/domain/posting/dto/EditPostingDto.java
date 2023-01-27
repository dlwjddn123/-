package com.footstep.domain.posting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditPostingDto {

    @ApiModelProperty(value = "발자취 제목", required = true, example = "고양이")
    private String title;
    @ApiModelProperty(value = "발자취 내용", required = true, example = "귀여운 고양이")
    private String content;
    @ApiModelProperty(value = "발자취 게시일", required = true, example = "2023-01-23")
    private Date recordDate;
    private CreatePlaceDto createPlaceDto;
    @ApiModelProperty(value = "발자취 공개 여부", required = true, example = "1")
    private int visibilityStatusCode;
    private String imageUrl;
}
