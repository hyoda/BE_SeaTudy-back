package com.finalproject.seatudy.todoCategory.dto.response;

import com.finalproject.seatudy.todolist.dto.response.TodoListResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class TodoCategoryResponseDto {
    private Long categoryId;
    private String categoryName;
    private MemberCateDto memberCateDto;
    private List<TodoListResponseDto> todoList;
}
