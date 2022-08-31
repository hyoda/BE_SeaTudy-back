package com.finalproject.seatudy.todolist.service;

import com.finalproject.seatudy.dto.response.ResponseDto;
import com.finalproject.seatudy.entity.TodoList;
import com.finalproject.seatudy.todolist.dto.request.TodoListRequestDto;
import com.finalproject.seatudy.todolist.dto.response.TodoListResponseDto;
import com.finalproject.seatudy.todolist.repository.TodoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class TodoListService {

    private final TodoListRepository todolistRepository;
    public ResponseDto<?> getAlltodoList(TodoListRequestDto todoListRequestDto, HttpServletRequest request) {
        List<TodoList> list = todolistRepository.findAllBySelectDate(todoListRequestDto.getSelectDate());
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
    public ResponseDto<?> createTodoList(TodoListRequestDto todoListRequestDto, HttpServletRequest request)throws IOException{
    TodoList todoList = TodoList.builder()
            .selectDate(todoListRequestDto.getSelectDate())
            .content(todoListRequestDto.getContent())
            .build();
    todolistRepository.save(todoList);
    return ResponseDto.success(todoList);

    }
    public ResponseDto<?> updateTodoList(Long todoId,TodoListRequestDto todoListRequestDto){
        TodoList todolist = todolistRepository.findById(todoId).orElseThrow(
                () -> new NullPointerException("다시 해주세요!")
        );
        todolist.update(todoListRequestDto);
        return ResponseDto.success(todolist);

        }
    public ResponseDto<?> deleteTodoList(Long todoId){
        todolistRepository.deleteById(todoId);
        return ResponseDto.success("삭제가 완료되었습니다.");
    }

}
