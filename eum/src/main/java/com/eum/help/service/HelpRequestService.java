package com.eum.help.service;

import com.eum.help.vo.HelpRequestVO;
import com.eum.help.vo.RequestStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HelpRequestService {

    long create(HelpRequestVO vo);                   // 등록 후 PK 반환

    Optional<HelpRequestVO> getById(long id);        // 단건 조회 (없으면 empty)

    List<HelpRequestVO> getPage(int page, int size); // 페이징 목록: page=1부터, size>0

    void changeStatus(long id, RequestStatus status);// 상태 변경

    void scheduleAt(long id, LocalDateTime when);    // (선택) 예약시간 설정/변경
}
