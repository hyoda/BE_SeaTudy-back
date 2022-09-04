package com.finalproject.seatudy.todoCategory.repository;

import com.finalproject.seatudy.entity.TodoCategory;
import com.finalproject.seatudy.login.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoCategoryRepository extends JpaRepository<TodoCategory, Long> {

    List<TodoCategory> findAllByMember(Member member);
}
