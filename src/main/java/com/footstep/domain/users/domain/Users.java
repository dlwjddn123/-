package com.footstep.domain.users.domain;

import com.footstep.domain.base.BaseTimeEntity;
import com.footstep.domain.base.Status;
import com.footstep.domain.posting.domain.Comment;
import com.footstep.domain.posting.domain.Likes;
import com.footstep.domain.posting.domain.posting.Posting;
import com.footstep.domain.report.domain.Report;
import com.footstep.domain.users.dto.JoinDto;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Users extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "users_id")
    private Long id;
    private String email;
    private String nickname;
    private String password;
    private String phoneNumber;
    private String profileImageUrl;
    private int reportedCount;
    private Date bannedDate;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private Set<Authority> authorities = new HashSet<>();

    @Enumerated
    private Status status;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Posting> postings = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Report> reports = new ArrayList<>();

    public static Users ofUser(JoinDto joinDto) {
        Users member = Users.builder()
                .email(joinDto.getEmail())
                .password(joinDto.getPassword())
                .nickname(joinDto.getNickname())
                .status(Status.NORMAL)
                .reportedCount(0)
                .build();
        member.addAuthority(Authority.ofUser(member));
        return member;
    }

    public List<String> getRoles() {
        return authorities.stream()
                .map(Authority::getRole)
                .collect(Collectors.toList());
    }

    private void addAuthority(Authority authority) {
        authorities.add(authority);
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeProfileImage(String imageUrl) {
        this.profileImageUrl = imageUrl;
    }

    public void secession() {
        this.status = Status.EXPIRED;
    }

    public void addReportedCount() {
        this.reportedCount += 1;
    }

    public void initReportedCount() {
        this.reportedCount = 0;
    }

    public void changeBannedDate(Date bannedDate) { this.bannedDate = bannedDate; }
}
