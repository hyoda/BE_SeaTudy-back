package com.finalproject.seatudy.entity;

import com.finalproject.seatudy.login.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "ranks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rankId;

    private String studyTime;

    @ManyToOne
    private Member member;


}
