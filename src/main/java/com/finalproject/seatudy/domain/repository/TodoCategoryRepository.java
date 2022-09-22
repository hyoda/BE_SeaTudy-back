package com.finalproject.seatudy.domain.repository;

import com.finalproject.seatudy.domain.entity.TodoCategory;
import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.entity.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoCategoryRepository extends JpaRepository<TodoCategory, Long> {

    List<TodoCategory> findAllByMember(Member member);
    List<TodoCategory> findAllBySelectDateContaining(String selectDate);
    List<TodoCategory> findAllByCategoryName(String categoryName);
    List<TodoCategory> findAllByCategoryNameAndSelectDate(String categoryName, String selectDate);

    //categoryId랑 Memberid를 동시에 만족하는 객체
    TodoCategory findByCategoryIdAndMember(Long categoryId, Member member);
}
