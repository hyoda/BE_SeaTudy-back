package com.finalproject.seatudy.service;

import com.finalproject.seatudy.security.exception.CustomException;
import com.finalproject.seatudy.security.exception.ErrorCode;
import com.finalproject.seatudy.service.dto.response.ResponseDto;
import com.finalproject.seatudy.domain.entity.TodoCategory;
import com.finalproject.seatudy.domain.entity.TodoList;
import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.repository.MemberRepository;
import com.finalproject.seatudy.security.UserDetailsImpl;
import com.finalproject.seatudy.service.dto.response.TodoCateShortResDto;
import com.finalproject.seatudy.domain.repository.TodoCategoryRepository;
import com.finalproject.seatudy.service.dto.request.TodoListRequestDto;
import com.finalproject.seatudy.service.dto.request.TodoListUpdateDto;
import com.finalproject.seatudy.domain.repository.TodoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.finalproject.seatudy.security.exception.ErrorCode.*;
import static com.finalproject.seatudy.service.dto.response.TodoListResponseDto.*;


@Service
@RequiredArgsConstructor
@Transactional
public class TodoListService {

    private final TodoListRepository todolistRepository;
    private final TodoCategoryRepository todoCategoryRepository;
    private final MemberRepository memberRepository;

    //todo 리스트 생성
    public ResponseDto<?> createTodoList(UserDetailsImpl userDetails,Long todoCategoryId,TodoListRequestDto todoListRequestDto){
        if(todoListRequestDto.getContent().isEmpty()){
            throw new CustomException(EMPTY_TODOLIST);
        }
        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        TodoCategory todoCategory = getTodoCategory(todoCategoryId);

        if(!todoListRequestDto.getSelectDate().equals(todoCategory.getSelectDate())){
            throw new CustomException(MISMATCH_SELECT_DATE);
        }

        List<TodoList> todoLists = todolistRepository.findAllBySelectDateAndTodoCategory_CategoryName(todoCategory.getSelectDate(), todoCategory.getCategoryName());
        if(todoLists.size()>0){
            for (TodoList todoList : todoLists) {
                if(todoList.getContent().equals(todoListRequestDto.getContent())){
                    throw new CustomException(DUPLICATE_TODOLIST);
                }
            }
        }
        if(todoCategory.getMember().getEmail().equals(member.getEmail())){
            TodoList todoList = TodoList.builder()
                    .selectDate(todoListRequestDto.getSelectDate())
                    .content(todoListRequestDto.getContent())
                    .todoCategory(todoCategory)
                    .member(member)
                    .build();
            todolistRepository.save(todoList);
            TodoListCateResDto todoListCateResDto = TodoListCateResDto.builder()
                    .todoId(todoList.getTodoId())
                    .content(todoList.getContent())
                    .selectDate(todoList.getSelectDate())
                    .categoryId(todoList.getTodoCategory().getCategoryId())
                    .build();
            return ResponseDto.success(todoListCateResDto);
        }
        throw new CustomException(TODOLIST_FORBIDDEN_POST);
    }

    //todo 리스트 수정
    public ResponseDto<?> updateTodoList(UserDetailsImpl userDetails, Long todoId, TodoListUpdateDto todoListUpdateDto){
        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        TodoList todoList = todolistRepository.findById(todoId).orElseThrow(
                () -> new CustomException(TODOLIST_NOT_FOUND)
        );

        if(todoList.getMember().getEmail().equals(member.getEmail())){
            todoList.update(todoListUpdateDto);

            TodoListCateResDto todoListCateResDto = TodoListCateResDto.builder()
                    .todoId(todoList.getTodoId())
                    .content(todoList.getContent())
                    .selectDate(todoList.getSelectDate())
                    .done(todoList.getDone())
                    .categoryId(todoList.getTodoCategory().getCategoryId())
                    .build();
            return ResponseDto.success(todoListCateResDto);
        }
        throw new CustomException(TODOLIST_FORBIDDEN_UPDATE);

    }

    //todo 리스트 삭제
    public ResponseDto<?> deleteTodoList(UserDetailsImpl userDetails,Long todoId){

        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        TodoList todolist = todolistRepository.findById(todoId).orElseThrow(
                () -> new CustomException(TODOLIST_NOT_FOUND)
        );

        if(member.getEmail().equals(todolist.getMember().getEmail())){
            todolistRepository.delete(todolist);
            return ResponseDto.success("삭제가 완료되었습니다.");
        }
        throw new CustomException(TODOLIST_FORBIDDEN_DELETE);
    }

    //todo 리스트 완료
    public ResponseDto<?> completeTodoList(UserDetailsImpl userDetails,Long todoId) {
        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        TodoList todoListDone = todolistRepository.findById(todoId).orElseThrow(
                () -> new CustomException(TODOLIST_NOT_FOUND)
        );

        if (member.getEmail().equals(todoListDone.getMember().getEmail())) {
            if (todoListDone.getDone() == 1) {
                todoListDone.cancelDone();
                TodoListCateResDto todoListCateResDto = TodoListCateResDto.builder()
                        .todoId(todoListDone.getTodoId())
                        .content(todoListDone.getContent())
                        .selectDate(todoListDone.getSelectDate())
                        .done(todoListDone.getDone())
                        .categoryId(todoListDone.getTodoCategory().getCategoryId())
                        .build();
                return ResponseDto.success(todoListCateResDto);
            }
            todoListDone.done();
            TodoListCateResDto todoListCateResDto = TodoListCateResDto.builder()
                    .todoId(todoListDone.getTodoId())
                    .content(todoListDone.getContent())
                    .selectDate(todoListDone.getSelectDate())
                    .done(todoListDone.getDone())
                    .categoryId(todoListDone.getTodoCategory().getCategoryId())
                    .build();
            return ResponseDto.success(todoListCateResDto);
        }
        throw new CustomException(TODOLIST_FORBIDDEN_COMPLETE);
    }
    //선택한 연 월 todolist 조회
    public ResponseDto<?> getTodoList(UserDetailsImpl userDetails,String selectDate) {
        List<TodoList> todoLists = todolistRepository.findAllBySelectDateContaining(selectDate);
        List<TodoCateResDto> todoCateResDtos = new ArrayList<>();

        Member member = memberRepository.findByEmail(userDetails.getMember().getEmail()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );

            for (TodoList todoList : todoLists){
                todoCateResDtos.add(TodoCateResDto.builder()
                        .todoCateShortResDto(TodoCateShortResDto.builder()
                                .todoCategoryId(todoList.getTodoCategory().getCategoryId())
                                .todoCategoryName(todoList.getTodoCategory().getCategoryName())
                                .selectDate(todoList.getTodoCategory().getSelectDate())
                                .build())
                        .todoId(todoList.getTodoId())
                        .content(todoList.getContent())
                        .selectDate(todoList.getSelectDate())
                        .done(todoList.getDone())
                        .build()
                );

            }
            return ResponseDto.success(todoCateResDtos);
    }

    private TodoCategory getTodoCategory(Long todoCategoryId){
        return todoCategoryRepository.findById(todoCategoryId).orElseThrow(
                () -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND)
        );
    }
}
