package com.myspring.eum.board.dao;

import java.util.HashMap;
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
	@Autowired
	private SqlSession sqlSession;
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
	public void updateArticle(ArticleVO article) throws Exception {
		sqlSession.update(NS + "updateArticle", article); // mapper: <update id="updateArticle"
															// parameterType="articleVO">
	}

	public void updateArticle(Map<String, Object> articleMap) throws Exception {
		sqlSession.update(NS + "updateArticleDynamic", articleMap); // mapper: <update id="updateArticleDynamic" ...>
	}

	@Override
	public int deleteArticle(Integer articleNO) {
		return sqlSession.delete(NS + "deleteArticle", articleNO);
	}

	// [추가]
	@Override
	public int countArticles(Map<String, Object> p) {
		return sqlSession.selectOne("mapper.board.countArticles", p);
	}

	// [추가]
	@Override
	public List<ArticleVO> selectPagedArticles(Map<String, Object> p, int offset, int size) {
		Map<String, Object> param = new HashMap<>();
		if (p != null)
			param.putAll(p); // 원본 맵 복사

		param.put("offset", Math.max(0, offset));
		param.put("size", Math.max(1, size));
		return sqlSession.selectList("mapper.board.selectPagedArticles", param);
	}

}
