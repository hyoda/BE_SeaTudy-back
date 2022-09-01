package com.finalproject.seatudy.dday.service;

import com.finalproject.seatudy.dday.dto.request.DdayRequestDto;
import com.finalproject.seatudy.dday.dto.response.DdayResponseDto;
import com.finalproject.seatudy.dday.repository.DdayRepository;
import com.finalproject.seatudy.dto.response.ResponseDto;
import com.finalproject.seatudy.entity.Dday;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DdayService {

    private final DdayRepository ddayRepository;

    //d-day 조회
    public ResponseDto<?> getAllDday(){
        List<Dday> ddays = ddayRepository.findAll();
        List<DdayResponseDto> ddayResponseDtos = new ArrayList<>();

        for(Dday dday : ddays) {
            ddayResponseDtos.add(DdayResponseDto.builder()
                    .id(dday.getId())
                    .title(dday.getTitle())
                    .dDay(dday.getDDay())
                    .build());
        }
        return ResponseDto.success(ddayResponseDtos);
    }

    //d-day 생성
    public ResponseDto<?> postDday(DdayRequestDto ddayRequestDto) {
        Dday dday = Dday.builder()
                .id(ddayRequestDto.getId())
                .title(ddayRequestDto.getTitle())
                .dDay(ddayRequestDto.getDDay())
                .build();
        Dday save = ddayRepository.save(dday);
        return ResponseDto.success(save);
    }

    //d-day 수정
    public ResponseDto<?> updateDday(Long ddayId, DdayRequestDto ddayRequestDto) {
        Dday dday = ddayRepository.findById(ddayId).orElseThrow(
                () -> new NullPointerException("해당 D-day가 없습니다.")
        );
        dday.update(ddayRequestDto);
        return ResponseDto.success(dday);
    }

    public ResponseDto<?> deleteDday(Long ddayId) {
        ddayRepository.deleteById(ddayId);
        return ResponseDto.success("삭제가 완료되었습니다.");
    }
}
