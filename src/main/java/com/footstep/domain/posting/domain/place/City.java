package com.footstep.domain.posting.domain.place;

import java.util.Arrays;

public enum City {
    SEOUL("서울특별시", 0),
    SEJONG("세종특별자치시", 1),
    INCHEON("인천광역시", 2),
    DAEJEON("대전광역시", 3),
    GWANGJU("광주광역시", 4),
    DAEGU("대구광역시", 5),
    ULSAN("울산광역시", 6),
    BUSAN("부산광역시", 7),
    GYEONGGI_DO("경기도", 8),
    GANGWON_DO("강원도", 9),
    CHUNGCHEONGBUK_DO("충청북도", 10),
    CHUNGCHEONGNAM_DO("충청남도", 11),
    JEOLLABUK_DO("전라북도", 12),
    JEOLLANAM_DO("전라남도", 13),
    GYEONGSANGBUK_DO("경상북도", 14),
    GYEONGSANGNAM_DO("경상남도", 15),
    JEJU("제주특별자치도", 16);

    private String cityName;
    private int cityCode;

    City(String cityName, int cityCode) {
        this.cityName = cityName;
        this.cityCode = cityCode;
    }

    public static City get(int code) {
        return Arrays.stream(City.values())
                .filter(c -> c.cityCode == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("없는 도시"));
    }

    public String getCityName() {
        return cityName;
    }

    public int getCityCode() {
        return cityCode;
    }
}
