package com.finalproject.seatudy.timeCheck.entity;

import com.finalproject.seatudy.Rank.Rank;
import com.finalproject.seatudy.login.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TimeCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checkId;

    @Column
    private String date;

    @Column
    private String checkIn;

    @Column
    private String checkOut;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "RANKS_ID")
    private Rank rank;

}
