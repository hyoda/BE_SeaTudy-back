package com.finalproject.seatudy.domain.repository;

import com.finalproject.seatudy.domain.entity.FishLocation;
import com.finalproject.seatudy.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FishLocationRepository extends JpaRepository<FishLocation, Long> {

    List<FishLocation> findAllByMember(Member member);
    Optional<FishLocation> findByMemberAndFishNum(Member member, int fishNum);
}
