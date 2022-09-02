package com.finalproject.seatudy.dto.response;

import com.finalproject.seatudy.login.LoginType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class KakaoUserDto {
    private Long id;
    private String email;
    private String nickname;
}
