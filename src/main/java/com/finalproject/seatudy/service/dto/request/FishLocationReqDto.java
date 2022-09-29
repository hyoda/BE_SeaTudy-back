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
    private Long fishNum;
    private int left;
    private int top;
}
