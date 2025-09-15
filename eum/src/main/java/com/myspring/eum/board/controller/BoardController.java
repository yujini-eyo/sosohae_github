package com.myspring.eum.board.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myspring.eum.board.vo.ArticleVO;

public interface BoardController {

	/** 목록 */
	ModelAndView listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception;

	/** 이미지 목록(옵션) */
	ModelAndView listImages(HttpServletRequest request, HttpServletResponse response) throws Exception;

	/** 답글 폼(옵션) */
	ModelAndView replyForm() throws Exception;

	/** 글쓰기 폼 */
	ModelAndView articleForm() throws Exception;

	/** 글 등록 (VO 기반, PRG) — 생성된 글번호로 처리되므로 Redirect 사용 */
	String addNewArticle(ArticleVO article, MultipartFile imageFile, HttpServletRequest request, HttpSession session,
			RedirectAttributes rttr) throws Exception;

	/** 상세보기 — (중요) Integer 으로 통일 */
	ModelAndView viewArticle(Integer articleNO, HttpServletRequest request, HttpServletResponse response)
			throws Exception;

	/** 글 수정 */
	ResponseEntity<String> modArticle(ArticleVO article, MultipartFile imageFile, String originalFileName,
			HttpServletRequest request) throws Exception;

	/** 글 삭제 (PRG) */
	ModelAndView removeArticle(Integer articleNO, HttpServletRequest request, HttpServletResponse response)
			throws Exception;

	// ===== (보존용) 구 Map 기반 등록 메서드 — 현재 미사용, 컴파일은 되게 주석 처리 =====
	// ModelAndView addNewArticle(MultipartHttpServletRequest multipartRequest,
	// HttpServletResponse response) throws Exception;
}
