package com.finalproject.seatudy.domain.entity;

import com.finalproject.seatudy.service.dto.request.FishLocationReqDto;
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
public class FishLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fishLocationId;
    private int leftValue;
    private int topValue;
    private int fishNum;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void update(FishLocationReqDto fishLocationReqDto){
        this.leftValue = fishLocationReqDto.getLeftValue();
        this.topValue = fishLocationReqDto.getTopValue();
    }

}
