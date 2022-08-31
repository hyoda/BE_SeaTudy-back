package com.finalproject.seatudy.entity;

import com.finalproject.seatudy.todolist.dto.request.TodoListRequestDto;
import com.finalproject.seatudy.todolist.dto.response.TodoListDoneDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TodoList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

    private String content;

    private String selectDate;

    private int done;

    public void update(TodoListRequestDto todoListRequestDto){
        this.content = todoListRequestDto.getContent();

    }

    public void done() {
        this.done = 1;
    }
}
