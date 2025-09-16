package com.myspring.eum.admin.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.myspring.eum.admin.vo.AdminArticleVO;

@Repository("boardAdminDAO")
public class BoardAdminDAOImpl implements BoardAdminDAO {

    private static final String NS = "mapper.adminBoard";

    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<AdminArticleVO> adminList(Map<String,Object> param) {
        return sqlSession.selectList(NS + ".adminList", param);
    }

    @Override
    public int admincountArticles(Map<String,Object> param) {
        Integer cnt = sqlSession.selectOne(NS + ".admincountArticles", param);
        return cnt == null ? 0 : cnt.intValue();
    }

    @Override
    public AdminArticleVO adminselectArticleByNo(int articleNO) {
        return sqlSession.selectOne(NS + ".adminselectArticleByNo", Integer.valueOf(articleNO));
    }

    @Override
    public int admininsertNewArticle(AdminArticleVO vo) {
        return sqlSession.insert(NS + ".admininsertNewArticle", vo);
    }

    @Override
    public int adminupdateArticle(AdminArticleVO vo) {
        return sqlSession.update(NS + ".adminupdateArticle", vo);
    }

    @Override
    public int adminupdateNoticeFlag(Map<String,Object> param) {
        return sqlSession.update(NS + ".adminupdateNoticeFlag", param);
    }

    @Override
    public int admindeleteArticle(int articleNO) {
        return sqlSession.delete(NS + ".admindeleteArticle", Integer.valueOf(articleNO));
    }

    @Override
    public int adminclearImage(int articleNO) {
        return sqlSession.update(NS + ".adminclearImage", Integer.valueOf(articleNO));
    }

    // ===== 공지 전용 =====
    @Override
    public List<AdminArticleVO> adminListNotice(Map<String, Object> params) throws Exception {
        return sqlSession.selectList(NS + ".adminListNotice", params); // ← 점 추가
    }

    @Override
    public int adminCountNotice(Map<String, Object> params) throws Exception {
        Integer n = sqlSession.selectOne(NS + ".adminCountNotice", params); // ← 점 추가
        return n == null ? 0 : n.intValue();
    }

    @Override
    public List<AdminArticleVO> adminRecentNotices(int limit) throws Exception {
        return sqlSession.selectList(NS + ".adminRecentNotices", Integer.valueOf(limit)); // ← 점 추가
    }
}
