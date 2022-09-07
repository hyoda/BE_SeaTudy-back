package com.finalproject.seatudy.service.dto.response;

import com.finalproject.seatudy.domain.entity.TodoList;
import lombok.*;

public class TodoListResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TodoListResDto {
        private Long todoId;
        private String content;
        private String selectDate;
        private int done;

        public TodoListResDto(TodoList todoList) {
            this.todoId = todoList.getTodoId();
            this.content = todoList.getContent();
            this.done = todoList.getDone();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TodoCateResDto {
        private TodoCateShortResDto todoCateShortResDto;
        private Long todoId;
        private String content;
        private String selectDate;
        private int done;
    }
}
