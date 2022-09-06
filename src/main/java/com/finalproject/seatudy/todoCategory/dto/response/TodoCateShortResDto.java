package com.finalproject.seatudy.todoCategory.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TodoCateShortResDto {
    private Long todoCategoryId;
    private String todoCategoryName;
}
