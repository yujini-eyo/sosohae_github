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

    public List<AdminArticleVO> adminList(Map<String,Object> param) {
        return sqlSession.selectList(NS + ".adminList", param);
    }

    public int admincountArticles(Map<String,Object> param) {
        Integer cnt = (Integer) sqlSession.selectOne(NS + ".admincountArticles", param);
        return cnt == null ? 0 : cnt.intValue();
    }

    public AdminArticleVO adminselectArticleByNo(int articleNO) {
        return (AdminArticleVO) sqlSession.selectOne(NS + ".adminselectArticleByNo", Integer.valueOf(articleNO));
    }

    public int admininsertNewArticle(AdminArticleVO vo) {
        return sqlSession.insert(NS + ".admininsertNewArticle", vo);
    }

    public int adminupdateArticle(AdminArticleVO vo) {
        return sqlSession.update(NS + ".adminupdateArticle", vo);
    }

    public int adminupdateNoticeFlag(Map<String,Object> param) {
        return sqlSession.update(NS + ".adminupdateNoticeFlag", param);
    }

    public int admindeleteArticle(int articleNO) {
        return sqlSession.delete(NS + ".admindeleteArticle", Integer.valueOf(articleNO));
    }

    public int adminclearImage(int articleNO) {
        return sqlSession.update(NS + ".adminclearImage", Integer.valueOf(articleNO));
    }
}
