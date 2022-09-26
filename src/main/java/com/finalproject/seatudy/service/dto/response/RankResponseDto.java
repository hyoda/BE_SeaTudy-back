package com.finalproject.seatudy.service.dto.response;

import com.finalproject.seatudy.domain.entity.Rank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RankResponseDto {
    private String nickname;
    private String email;
    private String dayStudy;

    public static RankResponseDto fromEntity(Rank rank) {

        return new RankResponseDto(
                rank.getMember().getNickname(),
                rank.getMember().getEmail(),
                rank.getDayStudy()
        );
    }

}
