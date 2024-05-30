package com.example.rework.dailyagenda.domain;

import com.example.rework.global.base.BaseTimeEntity;
import com.example.rework.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "DAILY_AGENDA")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DailyAgenda extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DAILY_AGENDA_ID")
    private Long id;

    @Column(length = 400)
    private String todo;

    @Column(length = 30, nullable = false)
    private boolean state;

    @Column(name = "PAGING_ID",nullable = false, unique = false)
    private Long pagingId;

    @JoinColumn(name = "MEMBER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void updatePagingId(Long pagingId) {
        this.pagingId = pagingId;
    }
}
