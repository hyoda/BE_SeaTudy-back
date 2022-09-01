package com.finalproject.seatudy.todolist.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TodoListResponseDto {
    private Long id;
    private String content;
    private String selectDate;
}
