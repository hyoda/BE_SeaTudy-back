package com.finalproject.seatudy.todolist.controller;

import com.finalproject.seatudy.dto.response.ResponseDto;
import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.todolist.dto.request.TodoListRequestDto;
import com.finalproject.seatudy.todolist.dto.request.TodoListUpdateDto;
import com.finalproject.seatudy.todolist.service.TodoListService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


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
    @PostMapping("/api/v1/{todoCategoryId}/todoLists")
    public ResponseDto<?> createTodoList(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long todoCategoryId, @RequestBody TodoListRequestDto todoListRequestDto){
        return todolistService.createTodoList(userDetails,todoCategoryId,todoListRequestDto);
    }

    //todolist 내용 수정
    @PutMapping("/api/v1/todoLists/{todoId}")
    public ResponseDto<?> updateTodoList(@AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable Long todoId,@RequestBody TodoListUpdateDto todoListUpdateDto){
        return todolistService.updateTodoList(userDetails,todoId,todoListUpdateDto);
    }
    //todolist 삭제
    @DeleteMapping("/api/v1/todoLists/{todoId}")
    public ResponseDto<?> deleteTodoList(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long todoId){
        return todolistService.deleteTodoList(userDetails,todoId);
    }

    //todolist 완료
    @PostMapping("/api/v1/todoLists/{todoId}")
    public ResponseDto<?> completeTodoList(@AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable Long todoId){
        return todolistService.completeTodoList(userDetails,todoId);
    }

    //선택한 연 월 의 todolist 불러오기
    @GetMapping("/api/v1/todoLists")
    public ResponseDto<?> getTodoList(@RequestBody TodoListRequestDto todoListRequestDto){
        return todolistService.getTodoList(todoListRequestDto);
    }
}
