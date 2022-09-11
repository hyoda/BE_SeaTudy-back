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
        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(TODOLIST_FORBIDDEN_POST)
        );
        TodoCategory todoCategory = getTodoCategory(todoCategoryId);

        if(!todoListRequestDto.getSelectDate().equals(todoCategory.getSelectDate())){
            return ResponseDto.fail("MISMATCH_SELECT_DATE", "카테고리의 날짜와 일치하지 않습니다.");
        }

        // todo 리스트 selectDate랑 todoCategory selectDate랑 같아야함(조건문 생성) -> 아니면 오류
        if(!todoListRequestDto.getSelectDate().equals(todoCategory.getSelectDate())){
            return ResponseDto.fail("MISMATCH_SELECT_DATE","카테고리의 날짜와 일치하지 않습니다.");
        }
        TodoList todoList = TodoList.builder()
                .selectDate(todoListRequestDto.getSelectDate())
                .content(todoListRequestDto.getContent())
                .todoCategory(todoCategory)
                .member(member)
                .build();
        todolistRepository.save(todoList);
        TodoListResDto todoListResDto = TodoListResDto.builder()
                .todoId(todoList.getTodoId())
                .content(todoList.getContent())
                .selectDate(todoList.getSelectDate())
                .build();

        return ResponseDto.success(todoListResDto);

    }

    //todo 리스트 수정
    public ResponseDto<?> updateTodoList(UserDetailsImpl userDetails, Long todoId, TodoListUpdateDto todoListUpdateDto){
        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(TODOLIST_FORBIDDEN_UPDATE)
        );
        TodoList todoList = todolistRepository.findById(todoId).orElseThrow(
                () -> new CustomException(TODOLIST_NOT_FOUND)
        );

        todoList.update(todoListUpdateDto);

        TodoListResDto todoListResDto = TodoListResDto.builder()
                .todoId(todoList.getTodoId())
                .content(todoList.getContent())
                .selectDate(todoList.getSelectDate())
                .done(todoList.getDone())
                .build();
        return ResponseDto.success(todoListResDto);

    }

    //todo 리스트 삭제
    public ResponseDto<?> deleteTodoList(UserDetailsImpl userDetails,Long todoId){

        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(TODOLIST_FORBIDDEN_DELETE)
        );
        TodoList todolist = todolistRepository.findById(todoId).orElseThrow(
                () -> new CustomException(TODOLIST_NOT_FOUND)
        );

        todolistRepository.delete(todolist);
        return ResponseDto.success("삭제가 완료되었습니다.");
    }

    //todo 리스트 완료
    public ResponseDto<?> completeTodoList(UserDetailsImpl userDetails,Long todoId) {
        Member member = memberRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(TODOLIST_FORBIDDEN_COMPLETE)
        );
        TodoList todoListDone = todolistRepository.findById(todoId).orElseThrow(
                () -> new CustomException(TODOLIST_NOT_FOUND)
        );

        if(todoListDone.getDone()==1){
            todoListDone.cancelDone();
            return ResponseDto.success("취소하였습니다");
        }
        todoListDone.done();
        return ResponseDto.success("할일을 완료하였습니다.");
    }
    //선택한 연 월 todolist 조회
    public ResponseDto<?> getTodoList(UserDetailsImpl userDetails,String selectDate) {
        List<TodoList> todoLists = todolistRepository.findAllBySelectDateContaining(selectDate);
        List<TodoCateResDto> todoCateResDtos = new ArrayList<>();

        Member member = memberRepository.findByEmail(userDetails.getMember().getEmail()).orElseThrow(
                () -> new CustomException(TODOLIST_FORBIDDEN_GET)
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
