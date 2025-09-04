package com.myspring.eum.admin.dao;

import java.util.List;
import java.util.Map;

import com.myspring.eum.board.vo.ArticleVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("adminBoardDAO")
public class AdminBoardDAOImpl implements AdminBoardDAO {

    private static final String NS = "mapper.adminBoard.";

    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<ArticleVO> selectAllArticles(Map<String, Object> param) throws Exception {
        return sqlSession.selectList(NS + "selectAllArticles", param);
    }

    @Override
    public ArticleVO selectArticle(int articleNO) throws Exception {
        return sqlSession.selectOne(NS + "selectArticle", articleNO);
    }

    @Override
    public int insertNewArticle(Map<String, Object> articleMap) throws Exception {
        return sqlSession.insert(NS + "insertNewArticle", articleMap);
    }

    @Override
    public int deleteArticle(int articleNO) throws Exception {
        return sqlSession.delete(NS + "deleteArticle", articleNO);
    }
}
