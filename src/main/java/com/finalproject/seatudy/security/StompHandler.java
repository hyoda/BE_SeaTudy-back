package com.finalproject.seatudy.security;

import com.finalproject.seatudy.domain.entity.Member;
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

import java.util.List;
import java.util.Optional;

import static com.finalproject.seatudy.service.dto.request.ChatMessageDto.MessageType;
import static com.finalproject.seatudy.service.dto.response.MemberResDto.ChatMemberRankDto;

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

            MessageType status;
            int userCount = chatRoomRepository.getUserCount(roomId);
            if(userCount == 2) {
                log.info(">>>> current # of user: {}", userCount);
                status = MessageType.FULL;
            } else status = MessageType.ENTER;

            String sessionId = (String) message.getHeaders().get("simpSessionId");
            Member member = jwtDecoder.getMember(token);
            chatRoomRepository.setUserEnterInfo(sessionId, roomId,member.getNickname());

            if(!chatRoomRepository.isAlreadyExist(roomId, member.getNickname())) {
                List<ChatMemberRankDto> rankListByNickname = chatRoomRepository.liveRankInChatRoom(roomId);
                log.info(">>>>>>> RankList: {}", rankListByNickname);
                log.info("SUBSCRIBE: session_{} / roomId_{}", sessionId, roomId);
                log.info("SUBSCRIBE: {}님 '{}'입장", member.getNickname(), roomId);
                chatRoomService.sendChatMessage(
                        ChatMessageDto.builder()
                                .type(status)
                                .roomId(roomId)
                                .sender(member.getNickname())
                                .defaultFish(member.getDefaultFishUrl())
                                .rankByNickname(rankListByNickname)
                                .build()
                );
            }
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String roomId = chatRoomRepository.getUserEnterRoomId(sessionId);

            String nickname = chatRoomRepository.getMemberNickname(sessionId, roomId);
            chatRoomRepository.removeUserEnterInfo(sessionId, roomId);

            if(!chatRoomRepository.isStillExist(roomId, nickname)) {
                log.info("DISCONNECT: session_{} / roomID_{}", sessionId, roomId);
                log.info("SUBSCRIBE: {}님 {} 퇴장", nickname, roomId);
                chatRoomService.sendChatMessage(
                        ChatMessageDto.builder()
                                .type(MessageType.EXIT)
                                .roomId(roomId)
                                .sender(nickname)
                                .build()
                );
            }
        }
        return message;
    }


}
