package com.finalproject.seatudy.todolist.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TodoListDoneDto {
    private int done;
}
