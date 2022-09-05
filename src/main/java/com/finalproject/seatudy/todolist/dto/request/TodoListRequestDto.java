package com.finalproject.seatudy.todolist.dto.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoListRequestDto {

    private String selectDate;
    private String content;

}
