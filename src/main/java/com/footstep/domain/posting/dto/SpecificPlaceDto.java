package com.footstep.domain.posting.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecificPlaceDto {

    @ApiModelProperty(notes = "장소명", example = "서울역")
    private String name;
    @ApiModelProperty(notes = "최근 게시된 사진", example = "http://news.samsungdisplay.com/wp-content/uploads/2018/08/1.png")
    private String imageUrl;
    @ApiModelProperty(notes = "발자취 수", example = "2")
    private int postingCount;
}
