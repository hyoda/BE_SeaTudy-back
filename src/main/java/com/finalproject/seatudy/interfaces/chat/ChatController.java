package com.finalproject.seatudy.interfaces.chat;

import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.repository.ChatRoomRepository;
import com.finalproject.seatudy.security.exception.CustomException;
import com.finalproject.seatudy.security.exception.ErrorCode;
import com.finalproject.seatudy.security.jwt.JwtDecoder;
import com.finalproject.seatudy.service.ChatRoomService;
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

    private final ChatRoomRepository chatRoomRepository;
    private final JwtDecoder jwtDecoder;
    private final ChatRoomService chatRoomService;


    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDto message, @Header("Authorization") String token) {
        Member member = jwtDecoder.getMember(token);

        if(!message.getMessage().isEmpty() || message.getMessage() != null || !message.getMessage().equals("")) {
            message.setType(ChatMessageDto.MessageType.TALK);
            message.setSender(member.getNickname());
            message.setDefaultFish(member.getDefaultFishUrl());
            message.setUserCount(chatRoomRepository.getUserCount(message.getRoomId()));
            log.info("사용자가 채팅방에 메시지전송 -- {}:{}",message.getSender(), message.getMessage());
            chatRoomService.sendChatMessage(message);
        } else throw new CustomException(ErrorCode.EMPTY_MESSAGE);
    }

}
