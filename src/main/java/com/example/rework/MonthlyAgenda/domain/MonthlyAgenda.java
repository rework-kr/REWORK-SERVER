package com.example.rework.MonthlyAgenda.domain;

import com.example.rework.global.base.BaseTimeEntity;
import com.example.rework.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;


@Entity(name = "MONTHLY_AGENDA")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MonthlyAgenda extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MONTHLY_AGENDA_ID")
    private Long id;
    @Column(length = 400)
    private String todo;
    @Column(length = 30, nullable = false)
    private boolean state;
    @JoinColumn(name = "MEMBER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void setTodo(String todo) {
        this.todo = todo;
    }
}
