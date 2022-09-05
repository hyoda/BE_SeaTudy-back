package com.finalproject.seatudy.entity;

import com.finalproject.seatudy.login.Member;
import com.finalproject.seatudy.todoCategory.dto.request.TodoCategoryRequestDto;
import lombok.*;

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
    private Long categoryId;

    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @OneToMany(mappedBy = "todoCategory",fetch = FetchType.EAGER ,cascade=CascadeType.ALL, orphanRemoval = true)
    private List<TodoList> todoList = new ArrayList<>();

    public void update(TodoCategoryRequestDto todoCategoryRequestDto){
        this.categoryName = todoCategoryRequestDto.getCategoryName();
    }

}
