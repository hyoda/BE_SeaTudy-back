package com.finalproject.seatudy.service.dto.response;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCateDto {
    private Long memberId;
    private String email;
}
