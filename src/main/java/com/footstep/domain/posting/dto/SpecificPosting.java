package com.footstep.domain.posting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecificPosting {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date postingDate;  //1. 포스트 날짜
    private String postingName;//2. 발자취 제목
    private String content; //3. 발자취 설명
    private String imageUrl; //4. 발자취 사진
    private String placeName;   //5. 발자취 장소(위치)
    private String likeNum; //6. 발자취 좋아요 갯수
    private String nickName;    //7. 발자취 작성 유저 닉네임,
    private List<CommentDto> commentList; //8. 댓글 리스트
    private String commentNum;  //9. 해당 발자취의 댓글 수
}
