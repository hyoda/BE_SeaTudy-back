package com.finalproject.seatudy.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "ranks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rankId;

    // 일일 총 공부한 시간
    @Column
    private String dayStudy;

    @Column
    private String totalStudy;

    @Column
    private String date;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "rank")
    private List<TimeCheck> timeChecks;
}
