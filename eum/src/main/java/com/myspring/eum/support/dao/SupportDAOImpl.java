package com.myspring.eum.support.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.myspring.eum.support.vo.SupportApplicationVO;

@Repository("supportDAO")
public class SupportDAOImpl implements SupportDAO {

    private static final String NS = "mapper.support";

    @Autowired
    private SqlSession sqlSession;

    @Override
    public boolean existsByArticleAndVolunteer(int articleNo, String volunteerId) {
        Map<String, Object> p = new HashMap<>();
        p.put("articleNo", articleNo);
        p.put("volunteerId", volunteerId);
        Integer cnt = sqlSession.selectOne(NS + ".existsByArticleAndVolunteer", p);
        return cnt != null && cnt > 0;
    }

    @Override
    public int insertApplication(SupportApplicationVO vo) {
        return sqlSession.insert(NS + ".insertApplication", vo);
    }

    @Override
    public int updateApplicationStatus(long applicationId, String status) {
        Map<String, Object> p = new HashMap<>();
        p.put("applicationId", applicationId);
        p.put("status", status);
        return sqlSession.update(NS + ".updateApplicationStatus", p);
    }

    @Override
    public List<SupportApplicationVO> findApplicantsByArticle(int articleNo) {
        return sqlSession.selectList(NS + ".findApplicantsByArticle", articleNo);
    }

    @Override
    public int bulkRejectOthers(int articleNo, long exceptApplicationId) {
        Map<String, Object> p = new HashMap<>();
        p.put("articleNo", articleNo);
        p.put("exceptApplicationId", exceptApplicationId);
        return sqlSession.update(NS + ".bulkRejectOthers", p);
    }

    @Override
    public int setArticleSelectedVolunteer(int articleNo, String volunteerId) {
        Map<String, Object> p = new HashMap<>();
        p.put("articleNo", articleNo);
        p.put("volunteerId", volunteerId); // null 허용
        return sqlSession.update(NS + ".setArticleSelectedVolunteer", p);
    }

    @Override
    public int updateArticleState(int articleNo, String state) {
        Map<String, Object> p = new HashMap<>();
        p.put("articleNo", articleNo);
        p.put("state", state);
        return sqlSession.update(NS + ".updateArticleState", p);
    }

    @Override
    public int increaseApplicantCount(int articleNo) {
        return sqlSession.update(NS + ".increaseApplicantCount", articleNo);
    }

    @Override
    public int decreaseApplicantCount(int articleNo) {
        return sqlSession.update(NS + ".decreaseApplicantCount", articleNo);
    }

    @Override
    public List<SupportApplicationVO> findByVolunteerId(String volunteerId) {
        return sqlSession.selectList(NS + ".findByVolunteerId", volunteerId);
    }

    @Override
    public SupportApplicationVO findById(long applicationId) {
        return sqlSession.selectOne(NS + ".findById", applicationId);
    }

    @Override
    public int articleOpenToInProgress(int articleNo) {
        return sqlSession.update(NS + ".articleOpenToInProgress", articleNo);
    }
}
