// SupportServiceImpl.java
// 역할: 도메인 규칙(중복 금지 등) 적용 + DAO 호출
package com.myspring.eum.support.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.myspring.eum.support.dao.SupportDAO;
import com.myspring.eum.support.vo.SupportApplicationVO;

@Service("supportService")
@Transactional
public class SupportServiceImpl implements SupportService {

	@Autowired
	private SupportDAO supportDAO;

	@Override
	public long apply(int articleNO, String volunteerId, String message) {
		// 같은 글에 같은 사람이 두 번 지원하지 못하도록 보호
		if (supportDAO.exists(articleNO, volunteerId) > 0) {
			throw new DuplicateKeyException("이미 지원함");
		}
		// 신규 지원 엔티티 조립
		SupportApplicationVO vo = new SupportApplicationVO();
		vo.setArticleNO(articleNO);
		vo.setVolunteerId(volunteerId);
		vo.setStatus("APPLIED"); // 초기 상태
		vo.setMessage(message);
		// 저장 후 PK 반환
		return supportDAO.insert(vo);
	}

	@Override
	public boolean exists(int articleNO, String volunteerId) {
		return supportDAO.exists(articleNO, volunteerId) > 0;
	}

	@Override
	public List<SupportApplicationVO> listByArticle(int articleNO) {
		return supportDAO.listByArticle(articleNO);
	}

	@Override
	public List<SupportApplicationVO> listMyApplications(String volunteerId) {
		return supportDAO.findByVolunteerId(volunteerId);
	}

	@Override
	public void select(long applicationId) {
		supportDAO.updateStatus(applicationId, "SELECTED");
	}

	@Override
	public void reject(long applicationId) {
		supportDAO.updateStatus(applicationId, "REJECTED");
	}

	@Override
	public void withdraw(long applicationId) {
		supportDAO.updateStatus(applicationId, "WITHDRAWN");
	}
}
