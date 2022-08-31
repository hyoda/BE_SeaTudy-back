package com.finalproject.seatudy.todolist.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoListRequestDto {

    private String selectDate;
    private String content;


        public TodoListRequestDto(String selectDate) {
        this.selectDate = selectDate;

            }

}
