package com.myspring.eum.support.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myspring.eum.board.vo.ArticleVO;
import com.myspring.eum.support.dao.SupportDAO;
import com.myspring.eum.support.vo.SupportApplicationVO;

@Service("supportService")
public class SupportServiceImpl implements SupportService {

	@Autowired
	private SupportDAO supportDAO;

	@Override
	public List<SupportApplicationVO> listApplicantsByArticle(int articleNo) {
		List<SupportApplicationVO> list = supportDAO.findApplicantsByArticle(articleNo);
		return (list != null) ? list : java.util.Collections.<SupportApplicationVO>emptyList();
	}

	@Override
	public List<SupportApplicationVO> listApplicantsToOwner(String ownerId) {
		List<SupportApplicationVO> list = supportDAO.findApplicantsToOwner(ownerId);
		return (list != null) ? list : java.util.Collections.<SupportApplicationVO>emptyList();
	}

	@Override
	public List<ArticleVO> listMyRequests(String writerId) {
		List<ArticleVO> list = supportDAO.findArticlesByWriter(writerId);
		return (list != null) ? list : java.util.Collections.<ArticleVO>emptyList();
	}

	@Override
	public List<SupportApplicationVO> listMyApplications(String volunteerId) {
		List<SupportApplicationVO> list = supportDAO.findByVolunteerId(volunteerId);
		return (list != null) ? list : java.util.Collections.<SupportApplicationVO>emptyList();
	}

	@Override
	public void apply(int articleNo, String volunteerId, String message) {
		// 유효성 정도만 간단 체크
		if (volunteerId == null || volunteerId.isEmpty()) {
			throw new IllegalArgumentException("volunteerId is required");
		}
		supportDAO.insertApplication(articleNo, volunteerId, message);
	}

	@Override
	public boolean exists(Integer articleNo, String volunteerId) {
		if (articleNo == null || volunteerId == null || volunteerId.isEmpty())
			return false;
		return supportDAO.existsApplication(articleNo, volunteerId);
	}

	@Override
	public List<SupportApplicationVO> listByArticle(Integer articleNo) {
		if (articleNo == null)
			return java.util.Collections.<SupportApplicationVO>emptyList();
		List<SupportApplicationVO> list = supportDAO.findApplicantsByArticle(articleNo);
		return (list != null) ? list : java.util.Collections.<SupportApplicationVO>emptyList();
	}
}
