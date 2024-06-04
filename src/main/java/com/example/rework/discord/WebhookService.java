package com.example.rework.discord;

import com.example.rework.global.error.InvalidDiscordMessage;
import com.example.rework.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class WebhookService {

    // 디스코드 웹훅 URL 주입
    @Value("${discord.webhook.url}")
    private String discordWebhookUrl;
    @Value("${discord.webhook.wating-approve}")
    private String discordWebhookUrl2;

    private final MemberRepository memberRepository;

    // 알림을 보내는 메서드
    @Transactional
    public boolean sendDiscordNotificationForRegister(String email) {
        try {
            // REST 요청을 처리하기 위한 RestTemplate 객체 생성
            RestTemplate restTemplate = new RestTemplate();

            // 회원 수를 기존 DB에서 조회하여 총 회원 수 계산
            Long totalMembers = memberRepository.count();

            // 알림 메시지 생성
            String message = " 🎉 Rework 서비스에 " + email + "님이 " + totalMembers + "번째로 회원가입 했습니다! 🎉";

            // HTTP 요청을 위한 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // HTTP 요청 바디에 전송할 메시지 설정 (Embed 구조)
            Map<String, Object> embed = new HashMap<>();
            embed.put("title", "New Member Signup");
            embed.put("description", message);
            embed.put("color", 65280); // Green color in decimal
            embed.put("footer", Map.of("text", "Rework Service Notification"));

            Map<String, Object> body = new HashMap<>();
            body.put("embeds", new Map[]{embed});

            // HTTP 요청 엔터티 생성
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 디스코드 웹훅 URL로 POST 요청을 보내어 알림을 전송
            restTemplate.postForEntity(discordWebhookUrl, requestEntity, String.class);

            return true;
        } catch (Exception e) {
            throw new InvalidDiscordMessage("디스코드 메시지 전송에 실패했습니다. " + e.getMessage());
        }
    }


    public boolean sendDiscordNotificationForNonMemberRegister(String email) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String message = "🎉 A new member signup request from " + email + "!";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> embed = new HashMap<>();
            embed.put("title", "New Signup Request");
            embed.put("description", message);
            embed.put("color", 16776960); // Yellow color in decimal
            embed.put("footer", Map.of("text", "Rework Service Notification"));

            Map<String, Object> body = new HashMap<>();
            body.put("embeds", new Map[]{embed});

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(discordWebhookUrl2, requestEntity, String.class);

            return true;
        } catch (Exception e) {
            throw new InvalidDiscordMessage("Failed to send Discord message: " + e.getMessage());
        }
    }
}