package com.footstep.domain.report.domain;

import java.util.Arrays;

public enum ReportTarget {

    USER(0),
    POSTING(1),
    COMMENT(2);

    private int targetCode;

    ReportTarget(int targetCode) {
        this.targetCode = targetCode;
    }

    public static ReportTarget get(int code) {
        return Arrays.stream(ReportTarget.values())
                .filter(c -> c.targetCode == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("신고 대상이 올바르지 않습니다."));
    }

    public int getTargetCode() { return targetCode; }
}
