package com.finalproject.seatudy.service;

import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.entity.TodoCategory;
import com.finalproject.seatudy.domain.repository.MemberRepository;
import com.finalproject.seatudy.domain.repository.TodoCategoryRepository;
import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.security.exception.CustomException;
import com.finalproject.seatudy.service.dto.request.TodoCategoryRequestDto;
import com.finalproject.seatudy.service.dto.response.ResponseDto;
import com.finalproject.seatudy.service.dto.response.TodoCategoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.finalproject.seatudy.security.exception.ErrorCode.*;


@RequiredArgsConstructor
@Transactional
@Service
public class TodoCategoryService {

    private final TodoCategoryRepository todoCategoryRepository;
    private final MemberRepository memberRepository;
    public ResponseDto<?> createTodoCategory(UserDetailsImpl userDetails, TodoCategoryRequestDto todoCategoryRequestDto){
        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        if(todoCategoryRequestDto.getCategoryName().isEmpty()) {
            throw new CustomException(EMPTY_CATEGORY);
        }
        List<TodoCategory> todoCategories = todoCategoryRepository.findAllByCategoryNameAndSelectDate(todoCategoryRequestDto.getCategoryName(),
                todoCategoryRequestDto.getSelectDate());

        if(todoCategories.size()>0) {
            for (TodoCategory category : todoCategories) {
                if(category.getCategoryName().equals(todoCategoryRequestDto.getCategoryName())){
                    throw new CustomException(DUPLICATE_CATEGORY);
                }
            }
        }
        TodoCategory todoCategory = TodoCategory.builder()
                .member(member)
                .categoryName(todoCategoryRequestDto.getCategoryName())
                .selectDate(todoCategoryRequestDto.getSelectDate())
                .build();
        todoCategoryRepository.save(todoCategory);

        TodoCategoryResponseDto todoCategoryResponseDto = TodoCategoryResponseDto.fromEntity(member, todoCategory);
        return ResponseDto.success(todoCategoryResponseDto);
    }

    public ResponseDto<?> getAllTodoCategory(UserDetailsImpl userDetails) {
        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        List<TodoCategory> todoCategories= todoCategoryRepository.findAllByMember(member);
        List<TodoCategoryResponseDto> todoCategoryResponseDtos = new ArrayList<>();

        for (TodoCategory todoCategory : todoCategories) {
            todoCategoryResponseDtos.add(TodoCategoryResponseDto.fromEntity(member, todoCategory));
        }
        return ResponseDto.success(todoCategoryResponseDtos);
    }

    public ResponseDto<?> updateTodoCategory(UserDetailsImpl userDetails, Long todoCategoryId, TodoCategoryRequestDto todoCategoryUpdateDto) {
        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        TodoCategory todoCategory = todoCategoryRepository.findById(todoCategoryId).orElseThrow(
                () -> new CustomException(CATEGORY_FORBIDDEN_UPDATE)
        );
        if (todoCategoryUpdateDto.getCategoryName().isEmpty()) {
            throw new CustomException(EMPTY_CATEGORY);
        }
        if(todoCategoryUpdateDto.getCategoryName().equals(todoCategory.getCategoryName())){
            throw new CustomException(CURRENT_CATEGORYNAME);
        }
        List<TodoCategory> todoCategories = todoCategoryRepository.findAllByCategoryNameAndSelectDate(todoCategoryUpdateDto.getCategoryName(), todoCategoryUpdateDto.getSelectDate());
        for (TodoCategory category : todoCategories) {
            if(category.getCategoryName().equals(todoCategoryUpdateDto.getCategoryName())){
                throw new CustomException(DUPLICATE_CATEGORY);
            }
        }

        if(member.getEmail().equals(todoCategory.getMember().getEmail())){
            todoCategory.update(todoCategoryUpdateDto);

            TodoCategoryResponseDto todoCategoryResponseDto = TodoCategoryResponseDto.fromEntity(member, todoCategory);

            return ResponseDto.success(todoCategoryResponseDto);
        }
        throw new CustomException(CATEGORY_FORBIDDEN_UPDATE);

    }

    public ResponseDto<?> getTodoCategory(UserDetailsImpl userDetails, String selectDate) {
        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        List<TodoCategory> todoCategories= todoCategoryRepository.findAllByMemberAndSelectDateContaining(member,selectDate);
        List<TodoCategoryResponseDto> todoCategoryResponseDtos = new ArrayList<>();

        for (TodoCategory todoCategory : todoCategories){
            todoCategoryResponseDtos.add(TodoCategoryResponseDto.fromEntity(member, todoCategory));
        }
        return ResponseDto.success(todoCategoryResponseDtos);
    }

    public ResponseDto<?> deleteTodoCategory(UserDetailsImpl userDetails, Long todoCategoryId) {
        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        TodoCategory todoCategory = todoCategoryRepository.findById(todoCategoryId).orElseThrow(
                () -> new CustomException(CATEGORY_NOT_FOUND)
        );
        if(member.getEmail().equals(todoCategory.getMember().getEmail())){
            todoCategoryRepository.delete(todoCategory);
            return ResponseDto.success("삭제가 완료되었습니다.");
        }
        throw new CustomException(CATEGORY_FORBIDDEN_DELETE);
    }
}
