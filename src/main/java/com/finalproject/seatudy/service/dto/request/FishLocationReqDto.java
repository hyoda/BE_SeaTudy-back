package com.finalproject.seatudy.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FishLocationReqDto {
    private int fishNum;
    private int leftValue;
    private int topValue;
}
