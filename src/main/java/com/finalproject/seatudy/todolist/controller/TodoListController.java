package com.finalproject.seatudy.todolist.controller;

import com.finalproject.seatudy.dto.response.ResponseDto;
import com.finalproject.seatudy.todolist.dto.request.TodoListRequestDto;
import com.finalproject.seatudy.todolist.service.TodoListService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class TodoListController {

    private final TodoListService todolistService;

    public TodoListController(TodoListService todolistService) {
        this.todolistService = todolistService;
    }

    @GetMapping("/api/v1/todoLists")
    public ResponseDto<?> getAlltodoList(@RequestBody TodoListRequestDto todoListRequestDto, HttpServletRequest request ){
        return todolistService.getAlltodoList(todoListRequestDto, request);
    }


    @PostMapping("/api/v1/todoLists")
    public ResponseDto<?> createTodoList(@RequestBody TodoListRequestDto todoListRequestDto, HttpServletRequest request )throws IOException {
        return todolistService.createTodoList(todoListRequestDto, request);
    }

    @PutMapping("/api/v1/todoLists/{todoId}")
    public ResponseDto<?> updateTodoList(@PathVariable Long todoId,@RequestBody TodoListRequestDto todoListRequestDto)throws IOException{
        return todolistService.updateTodoList(todoId,todoListRequestDto);
    }
    @DeleteMapping("/api/v1/todoLists/{todoId}")
    public ResponseDto<?> deleteTodoList(@PathVariable Long todoId){
        return todolistService.deleteTodoList(todoId);
    }

}
