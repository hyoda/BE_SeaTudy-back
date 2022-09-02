package com.finalproject.seatudy.Rank;

import com.finalproject.seatudy.login.Member;
import com.finalproject.seatudy.timeCheck.entity.TimeCheck;
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

    @Column
    private String studyTime;

    @Column
    private String date;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "rank")
    private List<TimeCheck> timeChecks;
}
