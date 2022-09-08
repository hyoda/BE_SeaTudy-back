package com.finalproject.seatudy.service.dto.response;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoCateShortResDto {
    private Long todoCategoryId;
    private String todoCategoryName;
    private String selectDate;
}
