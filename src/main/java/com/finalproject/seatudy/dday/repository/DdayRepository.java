package com.finalproject.seatudy.dday.repository;

import com.finalproject.seatudy.entity.Dday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DdayRepository extends JpaRepository<Dday, Long> {

}
