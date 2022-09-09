package com.finalproject.seatudy.service.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {
    private String roomId;
    private String sender;
    private String message;
}
