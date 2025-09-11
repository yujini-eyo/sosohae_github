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

    /** 수정: (현 구조 유지) Map 기반 동적 업데이트 */
    void modArticle(Map<String, Object> articleMap) throws Exception;

    /** 삭제 */
    void removeArticle(Integer articleNO) throws Exception;

    // (선택) 향후 VO 기반 수정으로 전환하고 싶다면 아래 시그니처 추가:
    // void modArticle(ArticleVO article) throws Exception;
}
