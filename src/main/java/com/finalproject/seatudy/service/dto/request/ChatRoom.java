package com.finalproject.seatudy.service.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Getter
@Setter
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;
    private String roomId;
    private String name;
    private long userCount;

    public static ChatRoom create(String name) {
        ChatRoom room = new ChatRoom();
        room.roomId = name;
        byte[] decode = Base64.getDecoder().decode(name);
        room.name = new String(decode, StandardCharsets.UTF_8);
        return room;
    }
}
