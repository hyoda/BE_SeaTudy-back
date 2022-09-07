package com.finalproject.seatudy.interfaces.chat;

import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.repository.MemberRepository;
import com.finalproject.seatudy.security.exception.CustomException;
import com.finalproject.seatudy.security.exception.ErrorCode;
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
    private final MemberRepository memberRepository;
    private final JwtDecoder jwtDecoder;
    /*
        stomp 방법 이용
     */

    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDto message, @Header("Authorization") String token) {
        Member member = getMemberNickname(token);

        chatRoomService.enterChatRoom(message.getRoomId());
        message.setMessage(member.getNickname() + "님이 채팅방에 참여하였습니다.");
        redisPublisher.publish(chatRoomService.getTopic(message.getRoomId()), message);
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDto message, @Header("Authorization") String token) {
        Member member = getMemberNickname(token);

        message.setSender(member.getNickname());
        log.info("사용자가 채팅방에 메시지전송 ----- {}:{}",message.getSender(), message.getMessage());
        redisPublisher.publish(chatRoomService.getTopic(message.getRoomId()), message);
    }

    private Member getMemberNickname(String token) {
        String accessToken = token.substring(7);
        String email = jwtDecoder.decodeUsername(accessToken);

        return memberRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
    }
}
