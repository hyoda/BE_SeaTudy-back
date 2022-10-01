package com.finalproject.seatudy.domain.repository;

import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.entity.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RankRepository extends JpaRepository<Rank, Long> {
    Optional<Rank> findByMemberAndDate(Member member, String date);
    List<Rank> findAllByMember(Member member);
    List<Rank> findTop20ByDateOrderByDayStudyDesc(String date);
    List<Rank> findAllByDateOrderByDayStudyDesc(String date);
    List<Rank> findAllByMemberAndWeek(Member member, int week);
    List<Rank> findAllByMemberAndDateContaining(Member member, String date);

}
