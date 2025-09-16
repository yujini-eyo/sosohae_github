package com.myspring.eum.admin.dao;

import java.util.List;
import java.util.Map;

import com.myspring.eum.admin.vo.AdminArticleVO;

public interface BoardAdminDAO {
	List<AdminArticleVO> adminList(Map<String, Object> param);

	int admincountArticles(Map<String, Object> param);

	AdminArticleVO adminselectArticleByNo(int articleNO);

	int admininsertNewArticle(AdminArticleVO vo);

	int adminupdateArticle(AdminArticleVO vo);

	int adminupdateNoticeFlag(Map<String, Object> param);

	int admindeleteArticle(int articleNO);

	int adminclearImage(int articleNO);

	List<AdminArticleVO> adminListNotice(Map<String, Object> params) throws Exception;

	int adminCountNotice(Map<String, Object> params) throws Exception;

	List<AdminArticleVO> adminRecentNotices(int limit) throws Exception;
}
