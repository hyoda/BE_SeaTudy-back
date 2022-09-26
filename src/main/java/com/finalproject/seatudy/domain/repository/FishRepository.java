package com.finalproject.seatudy.domain.repository;

import com.finalproject.seatudy.domain.entity.Fish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FishRepository extends JpaRepository<Fish, Long> {
    Fish findByFishName(String fishName);
    Fish findByFishId(Long id);
}
