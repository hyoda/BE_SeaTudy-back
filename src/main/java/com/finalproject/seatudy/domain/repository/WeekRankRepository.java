package com.finalproject.seatudy.domain.repository;

import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.entity.WeekRank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeekRankRepository extends JpaRepository<WeekRank, Long> {
    List<WeekRank> findAllByMember(Member member);
}
