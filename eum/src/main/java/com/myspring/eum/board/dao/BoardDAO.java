package com.myspring.eum.board.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.myspring.eum.board.vo.ArticleVO;


public interface BoardDAO {
	List<ArticleVO> selectAllArticlesList();
    int insertNewArticle(ArticleVO article);
    ArticleVO selectArticle(Integer articleNO);
    int updateArticle(Map<String, Object> articleMap);
    int deleteArticle(Integer articleNO);
}
