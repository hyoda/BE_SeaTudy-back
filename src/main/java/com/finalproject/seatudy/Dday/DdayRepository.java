package com.finalproject.seatudy.Dday;

import com.finalproject.seatudy.login.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DdayRepository extends JpaRepository<Dday, Long> {
    List<Dday> findAllByMember(Member member);
}
