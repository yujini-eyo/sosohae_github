package com.myspring.eum.support.service;

import java.util.List;

import com.myspring.eum.board.vo.ArticleVO;
import com.myspring.eum.support.vo.SupportApplicationVO;

public interface SupportService {
    List<SupportApplicationVO> listApplicantsByArticle(int articleNo);
    List<SupportApplicationVO> listApplicantsToOwner(String ownerId);
    void apply(int articleNO, String volunteerId, String message);
    List<ArticleVO>            listMyRequests(String writerId);
    
    // 컨트롤러 호환용 (추가)
    boolean exists(Integer articleNo, String volunteerId);
    List<SupportApplicationVO> listByArticle(Integer articleNo);
    
    // (옵션) 재사용
    List<SupportApplicationVO> listMyApplications(String volunteerId);
}
