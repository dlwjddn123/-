package com.footstep.domain.posting.domain.place;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public enum City {
    SEOUL("서울", 0),
    SEJONG("세종특별자치시", 1),
    INCHEON("인천", 2),
    DAEJEON("대전", 3),
    GWANGJU("광주", 4),
    DAEGU("대구", 5),
    ULSAN("울산", 6),
    BUSAN("부산", 7),
    GYEONGGI_DO("경기", 8),
    GANGWON_DO("강원", 9),
    CHUNGCHEONGBUK_DO("충북", 10),
    CHUNGCHEONGNAM_DO("충남", 11),
    JEOLLABUK_DO("전북", 12),
    JEOLLANAM_DO("전남", 13),
    GYEONGSANGBUK_DO("경북", 14),
    GYEONGSANGNAM_DO("경남", 15),
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

    public static Optional<City> getByName(String name) {
        return Arrays.stream(City.values())
                .filter(c -> Objects.equals(c.cityName, name))
                .findFirst();
    }

    public String getCityName() {
        return cityName;
    }

    public int getCityCode() {
        return cityCode;
    }
}
