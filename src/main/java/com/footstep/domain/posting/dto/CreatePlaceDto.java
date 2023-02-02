package com.footstep.domain.posting.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePlaceDto {

    @ApiModelProperty(value = "장소명", example = "경복궁")
    @NotBlank
    private String name;
    @ApiModelProperty(value = "주소", example = "서울 종로구 사직로 161")
    @NotBlank
    private String address;
    @ApiModelProperty(value = "위도", example = "37.5776087830657")
    @Min(-90) @Max(90)
    private Double latitude;
    @ApiModelProperty(value = "경도", example = "126.976896737645")
    @Min(-180) @Max(180)
    private Double longitude;
}
