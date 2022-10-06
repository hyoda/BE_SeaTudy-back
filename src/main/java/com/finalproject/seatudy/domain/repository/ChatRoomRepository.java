package com.finalproject.seatudy.domain.repository;

import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.entity.Rank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

import static com.finalproject.seatudy.service.dto.response.MemberResDto.ChatMemberRankDto;
import static com.finalproject.seatudy.service.util.CalendarUtil.totalTime;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ChatRoomRepository {

    private static final String ENTER_INFO = "ENTER_INFO";
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsEnterInfo;

    private final MemberRepository memberRepository;
    private final RankRepository rankRepository;

    public void setUserEnterInfo(String sessionId, String roomId, String nickname) {
        hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
        hashOpsEnterInfo.put(ENTER_INFO + "_" + roomId, sessionId, nickname);
    }

    public void removeUserEnterInfo(String sessionId, String roomId) {
        hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
        hashOpsEnterInfo.delete(ENTER_INFO + "_" + roomId, sessionId);
    }

    public boolean isAlreadyExist(String roomId, String nickname) {
        Map<String, String> entries = hashOpsEnterInfo.entries(ENTER_INFO + "_" + roomId);
        Collection<String> nicknameList = entries.values();

        int count = Collections.frequency(nicknameList, nickname);
        return count > 1;
    }

    public boolean isStillExist(String roomId, String nickname) {
        Map<String, String> entries = hashOpsEnterInfo.entries(ENTER_INFO + "_" + roomId);
        Collection<String> nicknameList = entries.values();

        int count = Collections.frequency(nicknameList, nickname);
        return count == 1;
    }

    public List<ChatMemberRankDto> liveRankInChatRoom(String roomId) {
        Map<String, String> entries = hashOpsEnterInfo.entries(ENTER_INFO + "_" + roomId);
        Collection<String> nicknameList = entries.values();
        Set<String> nicknameSetList = new HashSet<>(nicknameList);
        Map<String, Integer> pointMapList = new HashMap<>();



        for (String nickname : nicknameSetList) {
            pointMapList.put(nickname, memberRepository.findByNickname(nickname).get().getPoint());
        }

        List<String> pointList = new ArrayList<>(pointMapList.keySet());
        pointList.sort((v1, v2) -> pointMapList.get(v2).compareTo(pointMapList.get(v1)));

        List<ChatMemberRankDto> liveRankList = new ArrayList<>();
        for (String nickname : pointList) {
            Member member = memberRepository.findByNickname(nickname).get();
            List<Rank> allMemberList = rankRepository.findAllByMember(member);
            String total = totalTime(allMemberList);
            liveRankList.add(ChatMemberRankDto.builder()
                    .nickname(nickname)
                    .defaultFish(member.getDefaultFishUrl())
                    .totalStudy(total)
                    .point(pointMapList.get(nickname)).build());
        }
        return liveRankList;
    }

    public String getUserEnterRoomId(String sessionId) {
        return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
    }

    public String getMemberNickname(String sessionId, String roomId) {
        return hashOpsEnterInfo.get(ENTER_INFO + "_" + roomId, sessionId);
    }

    public int getUserCount(String roomId) {
        Map<String, String> entries = hashOpsEnterInfo.entries(ENTER_INFO + "_" + roomId);
        Collection<String> nicknameList = entries.values();
        Set<String> set = new HashSet<>(nicknameList);
        log.info("현재 Room_{} 인원수: {}", roomId, set.size());
        return set.size();
    }
}
