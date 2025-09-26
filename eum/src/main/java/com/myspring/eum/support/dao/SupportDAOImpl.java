package com.myspring.eum.support.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.myspring.eum.board.vo.ArticleVO;
import com.myspring.eum.support.vo.SupportApplicationVO;

@Repository("supportDAO")
public class SupportDAOImpl implements SupportDAO {

	@Autowired
	private SqlSession sqlSession;

	private static final String NS = "mapper.support.";

	@Override
	public List<SupportApplicationVO> findApplicantsByArticle(int articleNo) {
		return sqlSession.selectList(NS + "findApplicantsByArticle", articleNo);
	}

	@Override
	public List<SupportApplicationVO> findApplicantsToOwner(String ownerId) {
		return sqlSession.selectList(NS + "findApplicantsToOwner", ownerId);
	}

	@Override
	public List<ArticleVO> findArticlesByWriter(String writerId) {
		return sqlSession.selectList(NS + "findArticlesByWriter", writerId);
	}

	@Override
	public List<SupportApplicationVO> findByVolunteerId(String volunteerId) {
		return sqlSession.selectList(NS + "findByVolunteerId", volunteerId);
	}

	@Override
	public int insertApplication(int articleNo, String volunteerId, String message) {
		java.util.Map<String, Object> p = new java.util.HashMap<>();
		p.put("articleNo", articleNo);
		p.put("volunteerId", volunteerId);
		p.put("message", message);
		return sqlSession.insert(NS + "insertApplication", p);
	}

	@Override
	public boolean existsApplication(int articleNo, String volunteerId) {
		java.util.Map<String, Object> p = new java.util.HashMap<>();
		p.put("articleNo", articleNo);
		p.put("volunteerId", volunteerId);
		Integer cnt = sqlSession.selectOne(NS + "existsApplication", p);
		return cnt != null && cnt > 0;
	}
}
