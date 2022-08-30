package com.finalproject.seatudy.todolist.controller;

import com.finalproject.seatudy.dto.response.ResponseDto;
import com.finalproject.seatudy.todolist.service.TodoListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoListController {

    private final TodoListService todolistService;

    public TodoListController(TodoListService todolistService) {
        this.todolistService = todolistService;
    }

    @GetMapping("/api/v1/todoLists")
    public ResponseDto<?> getAlltodoList(){
        return todolistService.getAlltodoList();
    }
}
