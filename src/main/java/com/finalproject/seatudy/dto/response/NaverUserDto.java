package com.finalproject.seatudy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class NaverUserDto {
    private Long id;
    private String email;
    private String nickname;
    private String birth;
}

