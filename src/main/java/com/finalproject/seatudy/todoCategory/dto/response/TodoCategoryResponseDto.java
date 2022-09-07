package com.finalproject.seatudy.todoCategory.dto.response;

import lombok.*;

import java.util.List;

import static com.finalproject.seatudy.todolist.dto.response.TodoListResponseDto.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoCategoryResponseDto {
    private Long categoryId;
    private String categoryName;
    private MemberCateDto memberCateDto;
    private List<TodoListResDto> todoList;
}
