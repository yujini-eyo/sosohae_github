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

    @Autowired
    private BoardDAO boardDAO;

    /** 목록 */
    @Override
    public List<ArticleVO> listArticles() throws Exception {
        return boardDAO.selectAllArticlesList();
    }

    /**
     * 등록: insert 후 MyBatis가 채워준 article.articleNO 를 반환
     * (mapper insert 에 useGeneratedKeys="true" keyProperty="articleNO" 필수)
     */
    @Override
    public int addNewArticle(ArticleVO article) throws Exception {
        boardDAO.insertNewArticle(article);   // 반환값(영향 행수)은 사용하지 않음
        return article.getArticleNO();        // 생성 PK 반환
    }

    /** 상세 */
    @Override
    public ArticleVO viewArticle(Integer articleNO) throws Exception {
        return boardDAO.selectArticle(articleNO);
    }
    
    /** 수정 */
    @Override
    public void modArticle(ArticleVO article) throws Exception {
        // 한 곳에 로직을 모으기 위해 기존 update를 재사용
        updateArticle(article);
        // 또는 바로 DAO 호출도 가능:
        // boardDAO.updateArticle(article);
    }
    
    // ② VO 기반 수정(추가)
    @Override
    public void updateArticle(ArticleVO article) throws Exception {
        boardDAO.updateArticle(article);
    }

    /** 삭제 */
    @Override
    public void removeArticle(Integer articleNO) throws Exception {
        boardDAO.deleteArticle(articleNO);
        // (옵션) 자식/이미지 테이블 등이 있다면 여기서 함께 정리
        // boardDAO.deleteImagesByArticle(articleNO);
        // boardDAO.deleteRepliesByParent(articleNO);
    }

    // [추가]
    @Override
    public int countArticles(Map<String, Object> p) {
        return boardDAO.countArticles(p);
    }

    // [추가]
    @Override
    public List<ArticleVO> selectPagedArticles(Map<String, Object> p, int offset, int size) {
        return boardDAO.selectPagedArticles(p, offset, size);
    }

    // (선택) VO 기반 수정으로 마이그레이션하려면 DAO/mapper도 VO 파라미터로 맞추고 아래 추가:
    // @Override
    // public void modArticle(ArticleVO article) throws Exception {
    //     boardDAO.updateArticle(article);
    // }
}
