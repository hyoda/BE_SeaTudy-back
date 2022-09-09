package com.finalproject.seatudy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.seatudy.service.dto.request.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
            ChatMessageDto chatMessageDto = objectMapper.readValue(publishMessage, ChatMessageDto.class);

            log.info("메시지 채팅방에 전송 ---- {}:{}", chatMessageDto.getSender(), chatMessageDto.getMessage());
            simpMessagingTemplate.convertAndSend("/sub/chat/room/" + chatMessageDto.getRoomId(), chatMessageDto);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
