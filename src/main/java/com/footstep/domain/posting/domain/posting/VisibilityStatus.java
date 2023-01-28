package com.footstep.domain.posting.domain.posting;

import java.util.Arrays;

public enum VisibilityStatus {
    PUBLIC(0),
    PRIVATE(1);

    private int code;

    VisibilityStatus(int code) {
        this.code = code;
    }

    public static VisibilityStatus get(int code) {
        return Arrays.stream(VisibilityStatus.values())
                .filter(v -> v.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("없는 상태"));
    }

    public int getCode() {
        return code;
    }
}
