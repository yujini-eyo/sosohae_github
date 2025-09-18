// SupportDAOImpl.java
// 역할: 실제 MyBatis 매퍼를 호출하여 DB 접근을 수행
package com.myspring.eum.support.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.myspring.eum.support.vo.SupportApplicationVO;

@Repository("supportDAO")
public class SupportDAOImpl implements SupportDAO {

	@Autowired
	private SqlSessionTemplate sqlSession; // MyBatis 연동 객체

	@Override
	public long insert(SupportApplicationVO vo) {
		sqlSession.insert("mapper.support.insert", vo); // PK(applicationId) 채워짐
		return vo.getApplicationId();
	}

	@Override
	public int exists(int articleNO, String volunteerId) {
		Map<String, Object> p = new HashMap<>();
		p.put("articleNO", articleNO);
		p.put("volunteerId", volunteerId);
		return sqlSession.selectOne("mapper.support.existsByArticleAndVolunteer", p);
	}

	@Override
	public List<SupportApplicationVO> listByArticle(int articleNO) {
		return sqlSession.selectList("mapper.support.listByArticle", articleNO);
	}

	@Override
	public List<SupportApplicationVO> findByVolunteerId(String volunteerId) {
		return sqlSession.selectList("mapper.support.findByVolunteerId", volunteerId);
	}

	@Override
	public int updateStatus(long applicationId, String status) {
		Map<String, Object> p = new HashMap<>();
		p.put("applicationId", applicationId);
		p.put("status", status);
		return sqlSession.update("mapper.support.updateStatus", p);
	}

	@Override
	public int deleteById(long applicationId) {
		return sqlSession.delete("mapper.support.deleteById", applicationId);
	}
}
