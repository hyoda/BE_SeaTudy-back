package com.finalproject.seatudy.todolist.service;

import com.finalproject.seatudy.dto.response.ResponseDto;
import com.finalproject.seatudy.entity.TodoList;
import com.finalproject.seatudy.todolist.dto.response.TodoListResponseDto;
import com.finalproject.seatudy.todolist.repository.TodoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoListService {

    private final TodoListRepository todolistRepository;
    public ResponseDto<?> getAlltodoList() {
        List<TodoList> list = todolistRepository.findAll();
        List<TodoListResponseDto> todoListResponseDto = new ArrayList<>();

        for (TodoList todoList : list) {
            todoListResponseDto.add(TodoListResponseDto.builder()
                    .id(todoList.getTodoId())
                    .content(todoList.getContent())
                    .build()
            );
        }

        return ResponseDto.success(todoListResponseDto);

    }
}
