package com.myspring.eum.support.dao;

import java.util.List;
import com.myspring.eum.support.vo.SupportApplicationVO;

public interface SupportDAO {
    // 중복 신청 존재 여부
    boolean existsByArticleAndVolunteer(int articleNo, String volunteerId);

    // 신청 생성 (keyProperty는 매퍼에서 처리)
    int insertApplication(SupportApplicationVO vo);

    // 신청 상태 변경 (APPLIED/SELECTED/REJECTED/WITHDRAWN)
    int updateApplicationStatus(long applicationId, String status);

    // 글의 모든 지원자 목록
    List<SupportApplicationVO> findApplicantsByArticle(int articleNo);

    // 선정 시 나머지 APPLIED 일괄 거절
    int bulkRejectOthers(int articleNo, long exceptApplicationId);

    // 글에 선정자 기록 (null 허용)
    int setArticleSelectedVolunteer(int articleNo, String volunteerId);

    // 글 상태 변경 (OPEN/IN_PROGRESS/CLOSED 등)
    int updateArticleState(int articleNo, String state);

    // 지원자수 카운트 증감
    int increaseApplicantCount(int articleNo);
    int decreaseApplicantCount(int articleNo);

    // 내 신청 목록
    List<SupportApplicationVO> findByVolunteerId(String volunteerId);

    // 단건 조회 (cancel/select 시에 사용)
    SupportApplicationVO findById(long applicationId);

    // (옵션) OPEN -> IN_PROGRESS 전용 전이 (서비스 코드가 쓰고 있으면 유지)
    int articleOpenToInProgress(int articleNo);
}
