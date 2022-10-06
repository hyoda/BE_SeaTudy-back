package com.finalproject.seatudy.service.dto.response;

import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.entity.TodoCategory;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.finalproject.seatudy.service.dto.response.TodoListResponseDto.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoCategoryResponseDto {
    private Long categoryId;
    private String categoryName;
    private String selectDate;
    private MemberCateDto memberCateDto;
    private List<TodoListResDto> todoList;

    public static TodoCategoryResponseDto fromEntity(Member member, TodoCategory todoCategory) {
        TodoCategoryResponseDto todoCategoryResponseDto = new TodoCategoryResponseDto();
        todoCategoryResponseDto.categoryId = todoCategory.getCategoryId();
        todoCategoryResponseDto.categoryName = todoCategory.getCategoryName();
        todoCategoryResponseDto.selectDate = todoCategory.getSelectDate();
        todoCategoryResponseDto.memberCateDto = new MemberCateDto(member);

        if (todoCategory.getTodoList() != null) {
            todoCategoryResponseDto.todoList = todoCategory.getTodoList().stream().map(TodoListResDto::new).collect(Collectors.toList());
        }else todoCategoryResponseDto.todoList = new ArrayList<>();

        return todoCategoryResponseDto;
    }
}
