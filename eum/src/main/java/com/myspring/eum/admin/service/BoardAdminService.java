package com.myspring.eum.admin.service;

import java.util.List;
import java.util.Map;

import com.myspring.eum.admin.vo.AdminArticleVO;

public interface BoardAdminService {
	List<AdminArticleVO> adminList(Map<String, Object> param);

	int admincountArticles(Map<String, Object> param);

	AdminArticleVO adminselectArticleByNo(int articleNO);

	int adminaddNewArticle(AdminArticleVO vo);

	int adminupdateArticle(AdminArticleVO vo);

	int adminupdateNoticeFlag(int articleNO, boolean isNotice);

	int adminremoveArticle(int articleNO);

	int adminclearImage(int articleNO);

	List<AdminArticleVO> getNoticeList(String q, String sort, int page, int size) throws Exception;

	int getNoticeTotal(String q) throws Exception;
}
