package com.finalproject.seatudy.domain.repository;

import com.finalproject.seatudy.service.dto.request.ChatRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class ChatRoomRepository {

    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private static final String USER_COUNT = "USER_COUNT";
    private static final String ENTER_INFO = "ENTER_INFO";

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsEnterInfo;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;

    public List<ChatRoom> findAllChatRooms() {
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    //유저가 입장한 채팅서버ID와 유저 세션ID 맵핑 정보 저장
    public void setUserEnterInfo(String sessionId, String roomId, String nickname) {
        hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
        hashOpsEnterInfo.put(ENTER_INFO+"_"+roomId,sessionId, nickname);
    }

    //유저 세션으로 입장해 있는 채팅방 ID조회
    public String getUserEnterRoomId(String sessionId) {
        return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
    }

    public String getNickname(String sessionId, String roomId) {
        return hashOpsEnterInfo.get(ENTER_INFO+"_"+roomId,sessionId);
    }

    // 유저 세션으로 입장해 있는 채팅방 ID 삭제
    public void removeUserEnterInfo(String sessionId, String roomId) {
        hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
        hashOpsEnterInfo.delete(ENTER_INFO+"_"+roomId,sessionId);
    }

    public long getUserCount(String roomId) {
        return Long.parseLong(Optional.ofNullable(valueOps.get(USER_COUNT + "_" + roomId)).orElse("0"));
    }

    // 채팅방 입장한 유저수 +1
    public long increaseUserCount(String roomId) {
        return Optional.ofNullable(valueOps.increment(USER_COUNT+ "_" + roomId)).orElse(0L);
    }

    // 채팅방 입장한 유저수 -1
    public long decreaseUserCount(String roomId) {
        return Optional.ofNullable(valueOps.decrement(USER_COUNT+ "_" + roomId))
                .filter(count -> count > 0).orElse(0L);
    }


    //thymeleaf 확인용 임시 method
    public ChatRoom findRoomById(String id) {
        return opsHashChatRoom.get(CHAT_ROOMS,id);
    }
}
