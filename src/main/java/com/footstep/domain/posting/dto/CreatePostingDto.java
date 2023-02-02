package com.footstep.domain.posting.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.*;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostingDto {

    @ApiModelProperty(value = "발자취 제목", example = "고양이")
    @NotBlank
    private String title;
    @ApiModelProperty(value = "발자취 내용", example = "귀여운 고양이")
    @NotBlank
    private String content;
    @ApiModelProperty(value = "발자취 게시일", example = "2023-01-23")
    @PastOrPresent
    private Date recordDate;
    private CreatePlaceDto createPlaceDto;
    @ApiModelProperty(value = "발자취 공개 여부", example = "1")
    @Min(0) @Max(1)
    private int visibilityStatusCode;
}
