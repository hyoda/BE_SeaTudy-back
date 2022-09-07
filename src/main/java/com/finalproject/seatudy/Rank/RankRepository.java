package com.finalproject.seatudy.Rank;

import com.finalproject.seatudy.login.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RankRepository extends JpaRepository<Rank , Long> {
    Optional<Rank> findByMemberAndDate(Member member, String date);
    List<Rank> findByMember(Member member);
    List<Rank> findByDate(String date);

}
