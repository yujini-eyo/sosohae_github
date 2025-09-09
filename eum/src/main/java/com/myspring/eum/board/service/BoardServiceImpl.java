package com.myspring.eum.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.eum.board.dao.BoardDAO;
import com.myspring.eum.board.vo.ArticleVO;

@Service("boardService")
@Transactional
public class BoardServiceImpl implements BoardService {

    @Autowired private BoardDAO boardDAO;

    @Override
    @Transactional(readOnly = true)
    public List<ArticleVO> listArticles() throws Exception {
        return boardDAO.selectAllArticlesList();
    }

    @Override
    public int addNewArticle(Map<String, Object> articleMap) throws Exception {
        // mapper에서 useGeneratedKeys 또는 selectKey로 articleNO를 채워 넣도록 구성
        boardDAO.insertNewArticle(articleMap);
        Object key = articleMap.get("articleNO");
        return (key instanceof Number) ? ((Number) key).intValue() : 0;
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleVO viewArticle(long articleNO) throws Exception {
        return boardDAO.selectArticle(articleNO);
    }

    @Override
    public void modArticle(Map<String, Object> articleMap) throws Exception {
        boardDAO.updateArticle(articleMap);
    }

    @Override
    public void removeArticle(long articleNO) throws Exception {
        boardDAO.deleteArticle(articleNO);
        // (옵션) 자식/이미지 테이블이 있다면 여기서 함께 정리
        // boardDAO.deleteImagesByArticle(articleNO);
        // boardDAO.deleteRepliesByParent(articleNO);
    }
}
