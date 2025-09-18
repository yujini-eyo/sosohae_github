package com.myspring.eum.board.service;

import java.util.List;
import java.util.Map;
import com.myspring.eum.board.vo.ArticleVO;

public interface BoardService {

    /** 목록 */
    List<ArticleVO> listArticles() throws Exception;

    /** 등록: 생성된 articleNO 반환 (useGeneratedKeys 기반) */
    int addNewArticle(ArticleVO article) throws Exception;

    /** 상세 */
    ArticleVO viewArticle(Integer articleNO) throws Exception;

 // ① VO 기반 수정(추가)
    void updateArticle(ArticleVO article) throws Exception;

    // (레거시 유지 시) Map 기반 수정도 남겨둘 수 있음
    void modArticle(ArticleVO article) throws Exception;

    void removeArticle(Integer articleNO) throws Exception;
}
