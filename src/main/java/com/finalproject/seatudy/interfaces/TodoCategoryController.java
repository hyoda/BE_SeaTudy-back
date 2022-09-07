package com.finalproject.seatudy.interfaces;

import com.finalproject.seatudy.domain.entity.TodoCategory;
import com.finalproject.seatudy.service.dto.response.ResponseDto;
import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.service.dto.request.TodoCategoryRequestDto;
import com.finalproject.seatudy.service.TodoCategoryService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class TodoCategoryController {

    private final TodoCategoryService todoCategoryService;

    @PostMapping("/todoCategories")
    @ApiOperation(value = "할일 카테고리 생성")
    public ResponseDto<?> createTodoCategory (@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @RequestBody TodoCategoryRequestDto todoCategoryRequestDto) {
        return todoCategoryService.createTodoCategory(userDetails,todoCategoryRequestDto);
    }

    @GetMapping("/todoCategories")
    @ApiOperation(value = "할일 카테고리 조회")
    public ResponseDto<?> getAllTodoCategory(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return todoCategoryService.getAllTodoCategory(userDetails);
    }

    @GetMapping("/todoCategories/dates")
    @ApiOperation(value = "할일 카테고리 날짜별 조회")
    @ApiImplicitParam(name = "selectDate", value = "날짜 선택")
    public ResponseDto<?> getTodoCategory(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @RequestParam String selectDate){
        return todoCategoryService.getTodoCategory(userDetails, selectDate);
    }

    @PutMapping("/todoCategories/{todoCategoryId}")
    @ApiOperation(value = "할일 카테고리 수정")
    @ApiImplicitParam(name = "todoCategoryId", value = "할일 카테고리 아이디")
    public ResponseDto<?> updateTodoCategory(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @PathVariable Long todoCategoryId,
                                             @RequestBody TodoCategoryRequestDto todoCategoryRequestDto) {
        return todoCategoryService.updateTodoCategory(userDetails,todoCategoryId,todoCategoryRequestDto);
    }
}
