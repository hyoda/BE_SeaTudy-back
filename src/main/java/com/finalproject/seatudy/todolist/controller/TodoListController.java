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

    //해당 날짜 todolist 불러오기
//    @GetMapping("/api/v1/todoLists/selectday")
//    public ResponseDto<?> getAlltodoList(@RequestBody TodoListRequestDto todoListRequestDto, HttpServletRequest request ){
//        return todolistService.getAlltodoList(todoListRequestDto, request);
//    }


    //해당날짜에 todolist 만들기
    @PostMapping("/api/v1/todoLists")
    public ResponseDto<?> createTodoList(@RequestBody TodoListRequestDto todoListRequestDto, HttpServletRequest request )throws IOException {
        return todolistService.createTodoList(todoListRequestDto, request);
    }

    //todolist 내용 수정
    @PutMapping("/api/v1/todoLists/{todoId}")
    public ResponseDto<?> updateTodoList(@PathVariable Long todoId,@RequestBody TodoListRequestDto todoListRequestDto)throws IOException{
        return todolistService.updateTodoList(todoId,todoListRequestDto);
    }
    //todolist 삭제
    @DeleteMapping("/api/v1/todoLists/{todoId}")
    public ResponseDto<?> deleteTodoList(@PathVariable Long todoId){
        return todolistService.deleteTodoList(todoId);
    }

    //todolist 완료
    @PostMapping("/api/v1/todoLists/{todoId}")
    public ResponseDto<?> completeTodoList(@PathVariable Long todoId){
        return todolistService.completeTodoList(todoId);
    }

    //선택한 연 월 의 todolist 불러오기
    @GetMapping("/api/v1/todoLists")
    public ResponseDto<?> getTodoList(@RequestBody TodoListRequestDto todoListRequestDto){
        return todolistService.getTodoList(todoListRequestDto);
    }
}
