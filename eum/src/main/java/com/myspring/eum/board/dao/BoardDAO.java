package com.myspring.eum.board.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.myspring.eum.board.vo.ArticleVO;


public interface BoardDAO {
	List<ArticleVO> selectAllArticlesList();
	 // [추가] 페이징용
    int countArticles(Map<String, Object> p);
    List<ArticleVO> selectPagedArticles(Map<String, Object> p, int offset, int size);
    
    int insertNewArticle(ArticleVO article);
    ArticleVO selectArticle(Integer articleNO);
    void updateArticle(ArticleVO article) throws Exception;
    int deleteArticle(Integer articleNO);
    // [레거시 Map 기반]
    void updateArticle(Map<String, Object> articleMap) throws Exception;
}
