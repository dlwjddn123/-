package com.footstep.domain.posting.domain.place;

import java.util.Arrays;

public enum City {
    INCHEON("인천광역시", 0),
    DAEJEON("대전광역시", 1),
    GWANGJU("광주광역시", 2),
    DAEGU("대구광역시", 3),
    ULSAN("울산광역시", 4),
    BUSAN("부산광역시", 5),
    GYEONGGI_DO("경기도", 6),
    GANGWON_DO("강원도", 7),
    CHUNGCHEONGBUK_DO("충청북도", 8),
    CHUNGCHEONGNAM_DO("충청남도", 9),
    JEOLLABUK_DO("전라북도", 10),
    JEOLLANAM_DO("전라남도", 11),
    GYEONGSANGBUK_DO("경상북도", 12),
    GYEONGSANGNAM_DO("경상남도", 13);

    private String cityName;
    private int cityCode;

    City(String cityName, int cityCode) {
        this.cityName = cityName;
        this.cityCode = cityCode;
    }

    public static City get(int cityNumber) {
        return Arrays.stream(City.values())
                .filter(c -> c.cityCode == cityNumber)
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
