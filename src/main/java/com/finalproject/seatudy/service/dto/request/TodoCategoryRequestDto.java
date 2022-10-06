package com.finalproject.seatudy.service.dto.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoCategoryRequestDto {

    private String categoryName;
    private String selectDate;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TodoCategoryUpdateDto {

        private String content;

    }
}
