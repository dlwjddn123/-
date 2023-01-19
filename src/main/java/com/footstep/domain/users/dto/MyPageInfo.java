package com.footstep.domain.users.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageInfo {

    private String nickname;

    private int postingCount;
}
