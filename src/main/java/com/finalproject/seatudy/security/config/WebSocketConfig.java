package com.finalproject.seatudy.security.config;

import com.finalproject.seatudy.security.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Value("${front.base.url}")
    private String FRONT_BASE_URL;


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // SockJS Client or WebSocket이 HandShake Connection을 생성할 경로
        registry.addEndpoint("/api/v1/chat/connections")
                .setAllowedOrigins("http://localhost:8080", "http://localhost:3000",
                        FRONT_BASE_URL)
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/pub");
        // simpleBroker는 해당경로를 SUBSCRIBE하는 client 에게 메세지를 전달하는 작업 수행
        config.enableSimpleBroker("/sub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}
