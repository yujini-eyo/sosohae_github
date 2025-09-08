package com.myspring.eum.admin.dao;

import java.util.List;
import java.util.Map;

import com.myspring.eum.admin.vo.AdminArticleVO;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository("adminBoardDAO")
public class AdminBoardDAOImpl implements AdminBoardDAO {

    @Autowired
    private SqlSession sqlSession; // 또는 SqlSessionTemplate

    private static final String NS = "mapper.adminBoard"; // XML의 <mapper namespace>

    @Override
    public List<AdminArticleVO> selectAllArticles(Map<String, Object> param) {
        return sqlSession.selectList(NS + ".selectAllArticles", param);
    }

    @Override
    public AdminArticleVO selectArticle(int articleNO) {
        return sqlSession.selectOne(NS + ".selectArticle", articleNO);
    }

    @Override
    public int insertNewArticle(Map<String, Object> articleMap) {
        return sqlSession.insert(NS + ".insertNewArticle", articleMap);
    }

    @Override
    public int deleteArticle(int articleNO) {
        return sqlSession.delete(NS + ".deleteArticle", articleNO);
    }
}
