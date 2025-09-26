package com.myspring.eum.support.dao;

import java.util.List;
import com.myspring.eum.board.vo.ArticleVO;
import com.myspring.eum.support.vo.SupportApplicationVO;

public interface SupportDAO {
	List<SupportApplicationVO> findApplicantsByArticle(int articleNo);

	List<SupportApplicationVO> findApplicantsToOwner(String ownerId);

	List<ArticleVO> findArticlesByWriter(String writerId);

	int insertApplication(int articleNo, String volunteerId, String message);

	boolean existsApplication(int articleNo, String volunteerId);
	// (옵션) 내가 지원한 목록 — 다른 화면에서 재사용
	List<SupportApplicationVO> findByVolunteerId(String volunteerId);

}
