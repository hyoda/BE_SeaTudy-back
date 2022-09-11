package com.finalproject.seatudy.service.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;
    private String roomId;
    private String name;

    public static ChatRoom create(String name) {
        ChatRoom room = new ChatRoom();
        room.roomId = name;
        room.name = name;
        return room;
    }
}
