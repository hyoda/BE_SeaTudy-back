package com.finalproject.seatudy.todolist.dto.request;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoListRequestDto {

    private String selectDate;
    private String content;

}
