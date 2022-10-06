package com.finalproject.seatudy.domain.repository;

import com.finalproject.seatudy.domain.entity.MemberFish;
import com.finalproject.seatudy.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberFishRepository extends JpaRepository<MemberFish, Long> {

    List<MemberFish> findAllByMember(Member member);
    Optional<MemberFish> findByMemberAndFish_FishId(Member member, Long fishId);
}
