package com.footstep.domain.posting.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostingDto {

    private String title;
    private String content;
    private Date recordDate;
    private String imageUrl;
    private CreatePlaceDto createPlaceDto;
    private int visibilityStatusCode;
}
