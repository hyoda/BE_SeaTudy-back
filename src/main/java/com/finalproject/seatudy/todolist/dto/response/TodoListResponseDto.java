package com.finalproject.seatudy.todolist.dto.response;

import com.finalproject.seatudy.entity.TodoList;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TodoListResponseDto {
    private Long id;
    private String content;
    private String selectDate;
    private int done;

    public TodoListResponseDto(TodoList todoList) {
        this.id = todoList.getTodoId();
        this.content = todoList.getContent();
        this.selectDate = todoList.getSelectDate();
        this.done = todoList.getDone();
    }
}
