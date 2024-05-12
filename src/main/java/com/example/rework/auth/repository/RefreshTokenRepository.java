package com.example.rework.auth.repository;


import com.example.rework.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

// redis cli 조회 명령어 HGETALL refreshToken:kbsserver@naver.com
// ttl 조회 TTL refreshToken:kbsserver@naver.com
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    void deleteById(String username);

}
