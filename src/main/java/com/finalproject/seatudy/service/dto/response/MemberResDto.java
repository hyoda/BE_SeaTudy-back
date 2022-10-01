package com.finalproject.seatudy.service.dto.response;

import com.finalproject.seatudy.domain.LoginType;
import com.finalproject.seatudy.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberResDto {
    private Long id;
    private String email;
    private String nickname;
    private String defaultFish;
    private LoginType loginType;
    private int point;

    public static MemberResDto fromEntity(Member member, int point) {
        return new MemberResDto(
                member.getMemberId(),
                member.getEmail(),
                member.getNickname(),
                member.getDefaultFishUrl(),
                member.getLoginType(),
                point
        );
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberOauthResDto {
        private String email;
    }
}
