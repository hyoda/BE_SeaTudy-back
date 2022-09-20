package com.finalproject.seatudy.service;

import com.finalproject.seatudy.domain.repository.ChatRoomRepository;
import com.finalproject.seatudy.service.dto.request.ChatMessageDto;
import com.finalproject.seatudy.service.dto.request.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
    private final ChatRoomRepository chatRoomRepository;
    private final ChannelTopic channelTopic;
    @Value("${static.chatroom.name}")
    private List<String> ROOM_NAME_LIST;

    @PostConstruct
    public void init() {
        opsHashChatRoom = redisTemplate.opsForHash();

        for (String roomName : ROOM_NAME_LIST) {
            ChatRoom chatRoom = ChatRoom.create(roomName);
            opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
        }
    }

    public String getRoomId(String destination) {
        int lastIdx = destination.lastIndexOf('/');

        if (lastIdx != -1) return destination.substring(lastIdx + 1);
        else return "";
    }

    public void sendChatMessage(ChatMessageDto chatMessageDto) {
        chatMessageDto.setUserCount(chatRoomRepository.getUserCount(chatMessageDto.getRoomId()));
        if(ChatMessageDto.MessageType.ENTER.equals(chatMessageDto.getType())) {
            chatMessageDto.setMessage("'"+chatMessageDto.getSender()+"'"+"님이 입장하였습니다.");
            chatMessageDto.setSender("[NOTICE]");
        } else if(ChatMessageDto.MessageType.EXIT.equals(chatMessageDto.getType())) {
            chatMessageDto.setMessage("'"+chatMessageDto.getSender()+"'"+"님이 퇴장하였습니다.");
            chatMessageDto.setSender("[NOTICE]");
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageDto);
    }
}