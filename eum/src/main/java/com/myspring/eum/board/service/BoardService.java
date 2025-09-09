package com.myspring.eum.board.service;

import java.util.List;
import java.util.Map;
import com.myspring.eum.board.vo.ArticleVO;

public interface BoardService {
    List<ArticleVO> listArticles() throws Exception;
    int addNewArticle(Map<String, Object> articleMap) throws Exception;
    ArticleVO viewArticle(long articleNO) throws Exception;
    void modArticle(Map<String, Object> articleMap) throws Exception;
    void removeArticle(long articleNO) throws Exception;
}
