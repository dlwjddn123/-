package com.footstep.domain.posting.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePlaceDto {

    @ApiModelProperty(value = "장소명", required = true, example = "서울역")
    private String name;
    @ApiModelProperty(value = "주소", required = true, example = "서울")
    private String address;
    @ApiModelProperty(value = "위도", required = true, example = "126.57740680000002")
    private Double latitude;
    @ApiModelProperty(value = "경도", required = true, example = "33.453357700000005")
    private Double longitude;
}
