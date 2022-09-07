package com.finalproject.seatudy.security;

import com.finalproject.seatudy.security.jwt.JwtDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtDecoder jwtDecoder;

    //Websocket을 통해 들어온 요청이 처리 되기전 실행


    @Nullable
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // websocket 연결시 헤더dml jwt token 추출
        if(StompCommand.CONNECT == accessor.getCommand()) {
            jwtDecoder.isValidToken(accessor.getFirstNativeHeader("Authorization"));
        }
        return message;
    }
}
