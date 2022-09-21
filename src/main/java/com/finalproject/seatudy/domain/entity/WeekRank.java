package com.finalproject.seatudy.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "weekRanks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WeekRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weekRankId;

    // 일주일 공부한 시간
    @Column
    private String weekStudy;

    @Column
    private String totalStudy;

    @Column
    private String date;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
