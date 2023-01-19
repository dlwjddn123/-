package com.footstep.domain.posting.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllPlaceDto {

    @ApiModelProperty(notes = "장소 인덱스", example = "[1, 3, 6]")
    List<Long> placeId;
}
