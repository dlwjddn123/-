package com.footstep.domain.posting.util;

import java.util.HashMap;
import java.util.Map;

public class CityConverter {

    public static final Map<String, Integer> cityMapper = init();

    public static HashMap<String, Integer> init() {
        HashMap<String, Integer> mapper = new HashMap<>();
        mapper.put("서울", 0);
        mapper.put("세종", 1);
        mapper.put("인천", 2);
        mapper.put("대전", 3);
        mapper.put("광주", 4);
        mapper.put("대구", 5);
        mapper.put("울산", 6);
        mapper.put("부산", 7);
        mapper.put("경기", 8);
        mapper.put("강원", 9);
        mapper.put("충북", 10);
        mapper.put("충남", 11);
        mapper.put("전북", 12);
        mapper.put("전남", 13);
        mapper.put("경북", 14);
        mapper.put("경남", 15);
        mapper.put("제주", 16);
        return mapper;
    }

    public static int getCityCode(String address) {
        Integer cityCode = cityMapper.get(address);
        return cityCode;
    }
}
