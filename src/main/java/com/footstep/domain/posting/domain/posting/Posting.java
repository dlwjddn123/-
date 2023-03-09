package com.footstep.domain.posting.domain.posting;

import com.footstep.domain.base.BaseTimeEntity;
import com.footstep.domain.base.Status;
import com.footstep.domain.posting.domain.Comment;
import com.footstep.domain.posting.domain.Likes;
import com.footstep.domain.posting.domain.place.Place;
import com.footstep.domain.posting.dto.CreatePostingDto;
import com.footstep.domain.users.domain.Users;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import reactor.util.annotation.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.sql.Date;
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
    @Nullable
    private String imageUrl;
    private LocalDateTime modifiedDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date recordDate;
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
    public Posting(String title, String content, String imageUrl, Date recordDate, int visibilityStatusCode, Users users, Place place) {
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

    public void editPosting(CreatePostingDto createPostingDto, Place place, String imageUrl) {
        this.title = createPostingDto.getTitle();
        this.content = createPostingDto.getContent();
        this.imageUrl = imageUrl;
        this.recordDate = createPostingDto.getRecordDate();
        this.visibilityStatus = VisibilityStatus.get(createPostingDto.getVisibilityStatusCode());
        this.place = place;
    }

    public void removePosting() {
        this.status = Status.EXPIRED;
    }
}