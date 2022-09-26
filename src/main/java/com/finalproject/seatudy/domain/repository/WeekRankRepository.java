package com.finalproject.seatudy.domain.repository;

import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.entity.Rank;
import com.finalproject.seatudy.domain.entity.WeekRank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WeekRankRepository extends JpaRepository<WeekRank, Long> {
    List<WeekRank> findAllByMember(Member member);
    List<WeekRank> findTop20ByWeekOrderByWeekStudyDesc(int week);
    Optional<WeekRank> findByMemberAndWeek(Member member, int week);

}
