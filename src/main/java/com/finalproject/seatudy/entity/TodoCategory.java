package com.finalproject.seatudy.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.finalproject.seatudy.login.Member;
import com.finalproject.seatudy.todoCategory.dto.TodoCategoryRequestDto;
import lombok.*;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @OneToMany(mappedBy = "todoCategory")
    private List<TodoList> todoList = new ArrayList<>();

    public void update(TodoCategoryRequestDto todoCategoryRequestDto){
        this.categoryName = todoCategoryRequestDto.getCategoryName();
    }

}
