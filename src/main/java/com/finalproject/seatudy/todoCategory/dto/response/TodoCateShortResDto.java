package com.finalproject.seatudy.todoCategory.dto.response;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoCateShortResDto {
    private Long todoCategoryId;
    private String todoCategoryName;
}
