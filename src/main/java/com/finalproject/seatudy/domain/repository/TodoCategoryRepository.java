package com.finalproject.seatudy.domain.repository;

import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.entity.TodoCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoCategoryRepository extends JpaRepository<TodoCategory, Long> {

    List<TodoCategory> findAllByMember(Member member);
    List<TodoCategory> findAllByMemberAndSelectDateContaining(Member member, String selectDate);
    List<TodoCategory> findAllByCategoryNameAndSelectDate(String categoryName, String selectDate);
}
