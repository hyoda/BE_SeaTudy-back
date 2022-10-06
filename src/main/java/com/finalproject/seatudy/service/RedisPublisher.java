package com.finalproject.seatudy.service;

import com.finalproject.seatudy.service.dto.request.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    public void publish(ChannelTopic topic, ChatMessageDto chatMessageDto) {
        redisTemplate.convertAndSend(topic.getTopic(), chatMessageDto);
    }
}
