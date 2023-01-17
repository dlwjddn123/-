package com.footstep.domain.posting.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePlaceDto {

    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
}
