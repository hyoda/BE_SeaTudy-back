package com.finalproject.seatudy.service.dto.request;

import lombok.*;

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
    private long userCount;

    public enum MessageType {
        ENTER, EXIT, TALK
    }

}
