package com.finalproject.seatudy.interfaces;

import com.finalproject.seatudy.service.dto.response.ResponseDto;
import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.service.dto.request.TodoCategoryRequestDto;
import com.finalproject.seatudy.service.TodoCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TodoCategoryController {

    private final TodoCategoryService todoCategoryService;

    //todo 카테고리 생성
    @PostMapping("/api/v1/todoCategories")
    public ResponseDto<?> createTodoCategory (@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody TodoCategoryRequestDto todoCategoryRequestDto) {
        return todoCategoryService.createTodoCategory(userDetails,todoCategoryRequestDto);
    }

    //todo 카테고리 조회
    @GetMapping("/api/v1/todoCategories")
    public ResponseDto<?> getAllTodoCategory(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return todoCategoryService.getAllTodoCategory(userDetails);
    }

    //todo 카테고리 날짜별 조회
    @GetMapping("/api/v1/todoCategories/dates")
    public ResponseDto<?> getTodoCategory(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam String selectDate){
        return todoCategoryService.getTodoCategory(userDetails, selectDate);
    }

    //todo 카테고리 수정
    @PutMapping("/api/v1/todoCategories/{todoCategoryId}")
    public ResponseDto<?> updateTodoCategory(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long todoCategoryId, @RequestBody TodoCategoryRequestDto todoCategoryRequestDto) {
        return todoCategoryService.updateTodoCategory(userDetails,todoCategoryId,todoCategoryRequestDto);
    }

    //todo 카테고리 삭제
    @DeleteMapping("/api/v1/todoCategories/{todoCategoryId}")
    public ResponseDto<?> deleteTodoCategory(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long todoCategoryId){
        return todoCategoryService.deleteTodoCategory(userDetails, todoCategoryId);
    }
}
