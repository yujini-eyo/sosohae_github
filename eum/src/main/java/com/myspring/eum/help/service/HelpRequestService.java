package com.myspring.eum.help.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.myspring.eum.help.vo.HelpRequestVO;
import com.myspring.eum.help.vo.RequestStatus;

public interface HelpRequestService {

    long create(HelpRequestVO vo);                   // �벑濡� �썑 PK 諛섑솚

    Optional<HelpRequestVO> getById(long id);        // �떒嫄� 議고쉶 (�뾾�쑝硫� empty)

    List<HelpRequestVO> getPage(int page, int size); // �럹�씠吏� 紐⑸줉: page=1遺��꽣, size>0

    void changeStatus(long id, RequestStatus status);// �긽�깭 蹂�寃�

    void scheduleAt(long id, LocalDateTime when);    // (�꽑�깮) �삁�빟�떆媛� �꽕�젙/蹂�寃�
}
