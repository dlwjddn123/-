package com.footstep.domain.posting.domain.posting;

import com.footstep.domain.base.BaseTimeEntity;
import com.footstep.domain.base.Status;
import com.footstep.domain.posting.domain.Comment;
import com.footstep.domain.posting.domain.Likes;
import com.footstep.domain.posting.domain.place.Place;
import com.footstep.domain.users.domain.Users;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Posting extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "posting_id")
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private LocalDateTime modifiedDate;
    private LocalDateTime recordDate;
    @Enumerated(EnumType.STRING)
    private VisibilityStatus visibilityStatus;
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "posting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "posting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likeList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @Builder
    public Posting(String title, String content, String imageUrl, LocalDateTime recordDate, int visibilityStatusCode, Users users, Place place) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.modifiedDate = getExpiredDate();
        this.recordDate = recordDate;
        this.visibilityStatus = VisibilityStatus.get(visibilityStatusCode);
        this.status = Status.NORMAL;
        this.users = users;
        this.place = place;
    }
}