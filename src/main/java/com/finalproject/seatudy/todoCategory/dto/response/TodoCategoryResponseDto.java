package com.finalproject.seatudy.todoCategory.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.finalproject.seatudy.todolist.dto.response.TodoListResponseDto.*;

@Builder
@Getter
@Setter
public class TodoCategoryResponseDto {
    private Long categoryId;
    private String categoryName;
    private MemberCateDto memberCateDto;
    private List<TodoListResDto> todoList;
}
