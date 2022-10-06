package com.finalproject.seatudy.domain.entity;

import com.finalproject.seatudy.service.dto.request.FishLocationReqDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberFish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberFishId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "FISH_ID")
    private Fish fish;
    private int leftValue = 0;
    private int topValue = 0;

    public void update(FishLocationReqDto fishLocationReqDto){
        this.leftValue = fishLocationReqDto.getLeft();
        this.topValue = fishLocationReqDto.getTop();
    }

    public void resetLocation() {
        this.leftValue = 0;
        this.topValue = 0;
    }

}
