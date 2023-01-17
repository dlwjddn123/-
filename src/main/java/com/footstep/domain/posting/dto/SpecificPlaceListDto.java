package com.footstep.domain.posting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecificPlaceListDto {

    private String placeName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date recordDate;
    private String imageUrl;
    private String title;
    private Long likes;
    private Long postings;
    private Long postingId;
}
