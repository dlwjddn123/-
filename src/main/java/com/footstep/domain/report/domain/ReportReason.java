package com.footstep.domain.report.domain;

import java.util.Arrays;

public enum ReportReason {
    ABUSE("욕설이나 차별 혐오성 글", 0),
    LEWD("음란, 선정적으로 유해한 글", 1),
    ILLEGAL("불법 정보를 포함하는 글", 2),
    PAPERING("영리목적/스팸홍보/도배글", 3),
    PERSONAL_INFO_EXPOSURE("개인 정보를 노출하는 글", 4),
    VIOLENT("폭력/차별적/불쾌한 글", 5),
    ETC("기타", 6);

    private String reason;
    private int reasonCode;

    ReportReason(String reason, int reasonCode) {
        this.reason = reason;
        this.reasonCode = reasonCode;
    }

    public static ReportReason get(int code) {
        return Arrays.stream(ReportReason.values())
                .filter(c -> c.reasonCode == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("없는 신고 사유 코드 입니다."));
    }

    public String getReason() { return reason; }

    public int getReasonCode() { return reasonCode; }
}
