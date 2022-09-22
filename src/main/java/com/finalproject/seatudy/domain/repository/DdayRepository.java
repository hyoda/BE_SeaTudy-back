package com.finalproject.seatudy.domain.repository;

import com.finalproject.seatudy.domain.entity.Dday;
import com.finalproject.seatudy.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DdayRepository extends JpaRepository<Dday, Long> {
    List<Dday> findAllByMember(Member member);
    List<Dday> findAllByMemberAndTargetDay(Member member, String TargetDay);
}
