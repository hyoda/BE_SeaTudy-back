package com.finalproject.seatudy.entity;

import com.finalproject.seatudy.dday.dto.request.DdayRequestDto;
import com.finalproject.seatudy.todolist.dto.request.TodoListRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Dday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String dDay;

    @ManyToOne
    private Member memberId;

    public void update(DdayRequestDto ddayRequestDto){
        this.title = ddayRequestDto.getTitle();
        this.dDay = ddayRequestDto.getDDay();

    }

}
