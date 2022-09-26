package com.finalproject.seatudy.service.dto.response;

import com.finalproject.seatudy.domain.entity.Rank;
import com.finalproject.seatudy.domain.entity.WeekRank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WeekRankResponseDto {
    private String nickname;
    private String email;
    private String weekStudy;

    public static WeekRankResponseDto fromEntity(WeekRank weekRank) {

        return new WeekRankResponseDto(
                weekRank.getMember().getNickname(),
                weekRank.getMember().getEmail(),
                weekRank.getWeekStudy()
        );
    }
}
