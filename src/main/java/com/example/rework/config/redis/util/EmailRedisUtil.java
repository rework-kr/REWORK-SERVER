package com.example.rework.config.redis.util;

import com.example.rework.mail.MailPurpose;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EmailRedisUtil {
    private final StringRedisTemplate stringRedisTemplate;

    // key를 통해 value 리턴
    public List<String> getData(String key) {
        ListOperations<String, String> valueOperations = stringRedisTemplate.opsForList();
        long size = valueOperations.size(key); // Get the size of the list
        return valueOperations.range(key, 0, size - 1); // Get all elements of the list
    }
    public boolean existData(String key) {
        return stringRedisTemplate.hasKey(key);
    }


    // 유효 시간 동안(key, value)저장
//    public void setDataExpire(String key, Integer value, long duration) {
//        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
//        Duration expireDuration = Duration.ofSeconds(duration);
//        valueOperations.set(key, value.toString(), expireDuration);
//    }
    public void setListData(String key, String value, MailPurpose mailPurpose, long duration) {
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
        String data = value + "|" + mailPurpose;
        Duration expireDuration = Duration.ofSeconds(duration);

        listOperations.leftPush(key, data);
        stringRedisTemplate.expire(key, expireDuration);
    }

    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }

    public void setUpdatePasswordData(String email, long duration) {
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
        String data = email;
        Duration expireDuration = Duration.ofSeconds(duration);

        listOperations.leftPush(email, data);
        stringRedisTemplate.expire(email, expireDuration);
    }
}