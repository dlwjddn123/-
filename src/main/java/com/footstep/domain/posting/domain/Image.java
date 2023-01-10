package com.footstep.domain.posting.domain;

import com.footstep.domain.posting.domain.posting.Posting;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "posting_id")
    private Posting posting;

    @Builder
    public Image(String imageUrl, Posting posting) {
        this.imageUrl = imageUrl;
        this.posting = posting;
    }
}
