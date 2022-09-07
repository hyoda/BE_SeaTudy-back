package com.finalproject.seatudy.domain.repository;

import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.entity.TimeCheck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeCheckRepository extends JpaRepository<TimeCheck, Long> {
    List<TimeCheck> findByMemberAndDate(Member member, String date);
}
