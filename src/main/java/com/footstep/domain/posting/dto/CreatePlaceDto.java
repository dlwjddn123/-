package com.footstep.domain.posting.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePlaceDto {

    @ApiModelProperty(value = "장소명", required = true, example = "경복궁")
    private String name;
    @ApiModelProperty(value = "주소", required = true, example = "서울 종로구 사직로 161")
    private String address;
    @ApiModelProperty(value = "위도", required = true, example = "37.5776087830657")
    private Double latitude;
    @ApiModelProperty(value = "경도", required = true, example = "126.976896737645")
    private Double longitude;
}
