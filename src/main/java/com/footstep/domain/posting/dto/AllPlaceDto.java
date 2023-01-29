package com.footstep.domain.posting.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllPlaceDto {

    @ApiModelProperty(notes = "장소 인덱스", example = "1")
    private Long placeId;
    @ApiModelProperty(notes = "장소명", example = "경복궁")
    private String placeName;
    @ApiModelProperty(notes = "위도", example = "37.5776087830657")
    private Double latitude;
    @ApiModelProperty(notes = "경도", example = "126.976896737645")
    private Double longitude;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllPlaceDto that = (AllPlaceDto) o;
        return placeId.equals(that.placeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId);
    }
}
