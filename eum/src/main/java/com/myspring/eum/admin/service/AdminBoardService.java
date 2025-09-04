package com.myspring.eum.admin.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public interface AdminBoardService {
	ModelAndView listArticles(HttpServletRequest req, HttpServletResponse res) throws Exception;

	ResponseEntity<String> addNewArticle(MultipartHttpServletRequest req, HttpServletResponse res) throws Exception;

	ModelAndView viewArticle(int articleNO, HttpServletRequest req, HttpServletResponse res) throws Exception;

	ResponseEntity<String> removeArticle(int articleNO, HttpServletRequest req, HttpServletResponse res)
			throws Exception;
}
