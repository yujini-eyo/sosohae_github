package com.myspring.eum.admin.dao;

import java.util.List;
import java.util.Map;

import com.myspring.eum.board.vo.ArticleVO;

public interface AdminBoardDAO {

    /** 목록 조회 (필요 시 페이징: offset/limit) */
    List<ArticleVO> selectAllArticles(Map<String, Object> param) throws Exception;

    /** 단건 조회 */
    ArticleVO selectArticle(int articleNO) throws Exception;

    /** 신규 등록 */
    int insertNewArticle(Map<String, Object> articleMap) throws Exception;

    /** 삭제 */
    int deleteArticle(int articleNO) throws Exception;
}
