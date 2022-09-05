package com.finalproject.seatudy.Dday;

import com.finalproject.seatudy.login.Member;
import lombok.*;

import javax.persistence.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
public class Dday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ddayId;

    @Column
    private String title;

    @Column
    private String targetDay;

    @Column
    private Long dday;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;


    public void update(String title, String targetDay, Long dday){
        this.title = title;
        this.targetDay = targetDay;
        this.dday = dday;
    }
}
