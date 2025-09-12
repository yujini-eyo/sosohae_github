package com.myspring.eum.board.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.eum.board.vo.ArticleVO;
/*import com.myspring.eum.board.vo.ImageVO;*/


@Repository("boardDAO")
public class BoardDAOImpl implements BoardDAO {
    @Autowired private SqlSession sqlSession;
    private static final String NS = "mapper.board.";

    @Override
    public List<ArticleVO> selectAllArticlesList() {
        return sqlSession.selectList(NS + "selectAllArticlesList");
    }
    @Override
    public int insertNewArticle(ArticleVO article) {
        return sqlSession.insert(NS + "insertNewArticle", article);
    }
    @Override
    public ArticleVO selectArticle(Integer articleNO) {
        return sqlSession.selectOne(NS + "selectArticle", articleNO);
    }
    @Override
    public int updateArticle(Map<String, Object> articleMap) {
        return sqlSession.update(NS + "updateArticle", articleMap);
    }
    @Override
    public int deleteArticle(Integer articleNO) {
        return sqlSession.delete(NS + "deleteArticle", articleNO);
    }
}

