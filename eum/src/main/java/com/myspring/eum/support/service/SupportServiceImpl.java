package com.myspring.eum.support.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.eum.support.dao.SupportDAO;
import com.myspring.eum.support.vo.SupportApplicationVO;

@Service("supportService")
@Transactional(propagation = Propagation.REQUIRED)
public class SupportServiceImpl implements SupportService {

    @Autowired
    private SupportDAO supportDAO;

    @Override
    public void apply(int articleNo, String volunteerId, String message) {
        // 중복 신청 방지
        if (supportDAO.existsByArticleAndVolunteer(articleNo, volunteerId)) {
            throw new IllegalStateException("이미 신청하셨습니다.");
        }
        SupportApplicationVO vo = new SupportApplicationVO();
        vo.setArticleNo(articleNo);
        vo.setVolunteerId(volunteerId);
        vo.setStatus("APPLIED");
        vo.setMessage(message);

        supportDAO.insertApplication(vo);
        supportDAO.increaseApplicantCount(articleNo);
    }

    @Override
    public void cancel(long applicationId, String operatorId) {
        // 본인 신청만 취소 가능
        SupportApplicationVO vo = supportDAO.findById(applicationId);
        if (vo == null) throw new IllegalArgumentException("존재하지 않는 신청입니다.");
        if (!operatorId.equals(vo.getVolunteerId())) {
            throw new SecurityException("본인 신청만 취소할 수 있습니다.");
        }
        supportDAO.updateApplicationStatus(applicationId, "WITHDRAWN");
        supportDAO.decreaseApplicantCount(vo.getArticleNo());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupportApplicationVO> listApplicants(int articleNo, String requesterId) {
        // (필요시) requesterId가 글쓴이인지 검증 로직 추가
        return supportDAO.findApplicantsByArticle(articleNo);
    }

    @Override
    public void select(long applicationId, String requesterId) {
        // 선정: 대상 조회
        SupportApplicationVO vo = supportDAO.findById(applicationId);
        if (vo == null) throw new IllegalArgumentException("존재하지 않는 신청입니다.");

        int articleNo = vo.getArticleNo();
        String volunteerId = vo.getVolunteerId();

        // (필요시) requesterId가 해당 글의 작성자인지 검증 로직 추가

        // 선택자 SELECTED, 나머지 APPLIED -> REJECTED
        supportDAO.updateApplicationStatus(applicationId, "SELECTED");
        supportDAO.bulkRejectOthers(articleNo, applicationId);

        // 글 상태 전이 + 선정자 기록
        // 전용 메서드를 쓰고 있다면 아래 한 줄로 대체 가능:
        // supportDAO.articleOpenToInProgress(articleNo);
        supportDAO.updateArticleState(articleNo, "IN_PROGRESS");
        supportDAO.setArticleSelectedVolunteer(articleNo, volunteerId);
    }

    @Override
    public void reopen(int articleNo, String requesterId) {
        // (필요시) requesterId가 해당 글의 작성자인지 검증 로직 추가
        supportDAO.updateArticleState(articleNo, "OPEN");
        supportDAO.setArticleSelectedVolunteer(articleNo, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupportApplicationVO> listMyApplications(String volunteerId) {
        return supportDAO.findByVolunteerId(volunteerId);
    }
}
