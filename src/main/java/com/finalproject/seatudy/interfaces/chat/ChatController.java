package com.finalproject.seatudy.interfaces.chat;

import com.finalproject.seatudy.security.jwt.JwtDecoder;
import com.finalproject.seatudy.service.ChatRoomService;
import com.finalproject.seatudy.service.RedisPublisher;
import com.finalproject.seatudy.service.dto.request.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomService chatRoomService;

    private final JwtDecoder jwtDecoder;
    /*
        stomp 방법 이용
     */
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDto message, @Header("Authorization") String token) {
        String accessToken = token.substring(7);
        String username = jwtDecoder.decodeUsername(accessToken);

        chatRoomService.enterChatRoom(message.getRoomId());
        message.setMessage(username + "님이 채팅방에 참여하였습니다.");
        redisPublisher.publish(chatRoomService.getTopic(message.getRoomId()), message);
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDto message, @Header("Authorization") String token) {
        String accessToken = token.substring(7);
        String username = jwtDecoder.decodeUsername(accessToken);

        message.setSender(username);
        redisPublisher.publish(chatRoomService.getTopic(message.getRoomId()), message);
    }
}
