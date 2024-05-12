package com.example.rework.auth.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 14) // 2주 동안의 TTL 설정 (초 단위)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RefreshToken {
    @Id
    private String id; // member username
    private String token;
}
