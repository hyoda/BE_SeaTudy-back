package com.finalproject.seatudy.security;

import com.finalproject.seatudy.domain.repository.ChatRoomRepository;
import com.finalproject.seatudy.security.jwt.JwtDecoder;
import com.finalproject.seatudy.service.ChatRoomService;
import com.finalproject.seatudy.service.dto.request.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtDecoder jwtDecoder;
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String token = accessor.getFirstNativeHeader("Authorization");
        // websocket 연결시 헤더에서 jwt token 추출
        if(StompCommand.CONNECT == accessor.getCommand()) {
            jwtDecoder.isValidToken(token);
            log.info("CONNECT: {}", token);
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            String roomId = chatRoomService.getRoomId(Optional.ofNullable(
                    (String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));

            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String nickname = jwtDecoder.getMemberNickname(token).getNickname();

            chatRoomRepository.setUserEnterInfo(sessionId, roomId,nickname);
            chatRoomRepository.increaseUserCount(roomId);
            log.info("SUBSCRIBE: session_{} / roomId_{}", sessionId, roomId);
            log.info("SUBSCRIBE: # of user in room_{} -- {}", roomId, chatRoomRepository.getUserCount(roomId));

            log.info("SUBSCRIBE: {}님 '{}'입장", nickname, roomId);
            chatRoomService.sendChatMessage(
                    ChatMessageDto.builder()
                            .type(ChatMessageDto.MessageType.ENTER)
                            .roomId(roomId)
                            .sender(nickname)
                            .build()
            );
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String roomId = chatRoomRepository.getUserEnterRoomId(sessionId);

            String nickname = chatRoomRepository.getNickname(sessionId, roomId);
            chatRoomRepository.decreaseUserCount(roomId);
            chatRoomRepository.removeUserEnterInfo(sessionId, roomId);


            log.info("DISCONNECT: session_{} / roomID_{}", sessionId, roomId);
            log.info("DISCONNECT: # of user in room_{} -- {}", roomId, chatRoomRepository.getUserCount(roomId));

            log.info("SUBSCRIBE: {}님 {} 퇴장", nickname, roomId);
            chatRoomService.sendChatMessage(
                    ChatMessageDto.builder()
                            .type(ChatMessageDto.MessageType.EXIT)
                            .roomId(roomId)
                            .sender(nickname)
                            .build()
            );
        }
        return message;
    }


}
