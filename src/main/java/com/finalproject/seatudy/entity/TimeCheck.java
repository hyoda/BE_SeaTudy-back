package com.finalproject.seatudy.entity;

import com.finalproject.seatudy.login.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TimeCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checkId;

    private LocalDateTime checkinTime;

    private LocalDateTime checkoutTime;

    @ManyToOne
    private Member member;

}
