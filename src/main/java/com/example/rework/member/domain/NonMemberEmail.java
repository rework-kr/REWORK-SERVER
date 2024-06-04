package com.example.rework.member.domain;

import com.example.rework.auth.MemberRole;
import com.example.rework.global.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "NON_MEMBER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NonMemberEmail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NON_MEMBER_ID")
    private Long id;
    @Column(length = 100, nullable = false, unique = true)
    private String email;
    // 회원가입 승인 여부
    @Column(name = "IS_ACCEPTED", nullable = false)
    private boolean isAccepted;

    public void updateIsAccepted(boolean state) {
        this.isAccepted = state;
    }
}
