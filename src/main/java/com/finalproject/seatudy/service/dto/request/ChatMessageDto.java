package com.finalproject.seatudy.service.dto.request;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.finalproject.seatudy.service.dto.response.MemberResDto.ChatMemberRankDto;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private String defaultFish;
    private List<ChatMemberRankDto> rankByNickname = new ArrayList<>();
    private long userCount;

    public enum MessageType {
        ENTER, EXIT, TALK, FULL
    }
}
