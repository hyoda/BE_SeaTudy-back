package com.finalproject.seatudy.service.dto.response;

import lombok.*;

import java.util.List;

import static com.finalproject.seatudy.service.dto.response.TodoListResponseDto.*;

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
