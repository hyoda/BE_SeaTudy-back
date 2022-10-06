package com.finalproject.seatudy.service.dto.response;

import com.finalproject.seatudy.domain.entity.Member;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCateDto {
    private Long memberId;
    private String email;

    public MemberCateDto(Member member) {
        this.memberId = member.getMemberId();
        this.email = member.getEmail();
    }
}
