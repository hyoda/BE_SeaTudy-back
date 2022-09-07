package com.finalproject.seatudy.todolist.repository;

import com.finalproject.seatudy.entity.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {
    List<TodoList> findAllBySelectDateContaining(String selectDate);
}
