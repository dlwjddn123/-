package com.footstep.domain.report.domain;

import com.footstep.domain.base.BaseTimeEntity;
import com.footstep.domain.posting.domain.Comment;
import com.footstep.domain.posting.domain.posting.Posting;
import com.footstep.domain.users.domain.Users;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "report_id")
    private Long id;

    private ReportTarget reportTarget;

    private ReportReason reportReason;

    private Long targetId;

    private Long reportedUsersId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users;

    public Report(ReportTarget reportTarget, ReportReason reportReason, Long targetId, Long reportedUsersId, Users users) {
        this.reportTarget = reportTarget;
        this.reportReason = reportReason;
        this.targetId = targetId;
        this.reportedUsersId = reportedUsersId;
        this.users = users;
    }
}
