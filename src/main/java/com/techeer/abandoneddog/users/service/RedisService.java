package com.techeer.abandoneddog.users.service;

public interface RedisService {

    // 데이터 넣기
    void setValues(String username, String token);

    // 데이터 가져오기
    String getValues(String refresh);

    // 데이터 삭제
    void deleteValues(String refresh);
}
