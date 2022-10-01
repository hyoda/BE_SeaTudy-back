package com.finalproject.seatudy.service.dto.request;

import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<String, Integer> rankByNickname = new HashMap<>();
    private long userCount;

    public enum MessageType {
        ENTER, EXIT, TALK
    }
}
