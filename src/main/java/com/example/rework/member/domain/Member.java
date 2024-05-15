package com.example.rework.member.domain;


import com.example.rework.MonthlyAgenda.domain.MonthlyAgenda;
import com.example.rework.auth.MemberRole;
import com.example.rework.global.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity(name = "MEMBER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;
    @Column(length = 100, nullable = false, unique = true)
    private String userId;
    @Column(length = 200, nullable = false, unique = true)
    private String password;
    @Column(length = 30,nullable = false)
    private String name;
    @Column(length = 30,nullable = false)
    private MemberRole role; // ADMIN 관리자 - MANAGER 운영자 - MEMBER 일반회원
    @Column(length = 30,nullable = false)
    private boolean state;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MonthlyAgenda> monthlyAgendaList = new ArrayList<>();
}
