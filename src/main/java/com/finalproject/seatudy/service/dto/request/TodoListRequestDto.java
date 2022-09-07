package com.finalproject.seatudy.service.dto.request;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoListRequestDto {

    private String selectDate;
    private String content;

}
