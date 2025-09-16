package com.myspring.eum.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.myspring.eum.admin.dao.BoardAdminDAO;
import com.myspring.eum.admin.vo.AdminArticleVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("boardAdminService")
public class BoardAdminServiceImpl implements BoardAdminService {

	@Autowired
	private BoardAdminDAO boardAdminDAO;

	public List<AdminArticleVO> adminList(Map<String, Object> param) {
		return boardAdminDAO.adminList(param);
	}

	public int admincountArticles(Map<String, Object> param) {
		return boardAdminDAO.admincountArticles(param);
	}

	public AdminArticleVO adminselectArticleByNo(int articleNO) {
		return boardAdminDAO.adminselectArticleByNo(articleNO);
	}

	@Transactional
	public int adminaddNewArticle(AdminArticleVO vo) {
		int r = boardAdminDAO.admininsertNewArticle(vo);
		// MyBatis useGeneratedKeys → vo.setArticleNO(...) 채워짐
		return r;
	}

	@Transactional
	public int adminupdateArticle(AdminArticleVO vo) {
		return boardAdminDAO.adminupdateArticle(vo);
	}

	@Transactional
	public int adminupdateNoticeFlag(int articleNO, boolean isNotice) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("articleNO", Integer.valueOf(articleNO));
		m.put("isNotice", Boolean.valueOf(isNotice));
		return boardAdminDAO.adminupdateNoticeFlag(m);
	}

	@Transactional
	public int adminremoveArticle(int articleNO) {
		return boardAdminDAO.admindeleteArticle(articleNO);
	}

	@Transactional
	public int adminclearImage(int articleNO) {
		return boardAdminDAO.adminclearImage(articleNO);
	}

	// ===== [추가] 공지 전용 =====
	public List<AdminArticleVO> getNoticeList(String q, String sort, int page, int size) throws Exception {
		if (page < 1)
			page = 1;
		if (size < 1)
			size = 10;
		int offset = (page - 1) * size;

		Map<String, Object> m = new HashMap<String, Object>();
		m.put("q", q);
		m.put("sort", (sort == null || sort.length() == 0) ? "recent" : sort);
		m.put("limit", Integer.valueOf(size));
		m.put("offset", Integer.valueOf(offset));

		return boardAdminDAO.adminListNotice(m);
	}

	public int getNoticeTotal(String q) throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("q", q);
		return boardAdminDAO.adminCountNotice(m);
	}
}
