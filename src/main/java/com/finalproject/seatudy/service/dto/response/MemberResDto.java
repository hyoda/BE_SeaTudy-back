package com.finalproject.seatudy.service.dto.response;

import com.finalproject.seatudy.domain.LoginType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MemberResDto {
    private Long id;
    private String email;
    private String nickname;
    private String defaultFish;
    private LoginType loginType;
    private Long point;
}
