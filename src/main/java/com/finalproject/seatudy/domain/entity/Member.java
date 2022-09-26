package com.finalproject.seatudy.domain.entity;

import com.finalproject.seatudy.domain.LoginType;
import com.finalproject.seatudy.service.dto.request.NicknameReqDto;
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
public class Member extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column
    private String email;

    @Column
    private String nickname;

    @Column
    private String defaultFishUrl;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column
    private Long point;

    public void updateNickname(NicknameReqDto nicknameReqDto) {
        this.nickname = nicknameReqDto.getNickname();
    }

    public void updateDefaultFish(String fishName) {
        this.defaultFishUrl = fishName;
    }
}
