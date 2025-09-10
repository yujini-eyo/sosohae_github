package com.myspring.eum.board.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public interface BoardController {

    /** 게시글 목록 (DB 렌더) */
    ModelAndView listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /** (옵션) 이미지 목록 화면: ArticleVO.imageFileName 기반 */
    ModelAndView listImages(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /** 글 등록 (단일 이미지) */
    ModelAndView addNewArticle(MultipartHttpServletRequest multipartRequest,
                                         HttpServletResponse response) throws Exception;

    /** 글 상세 (단일 이미지 버전) */
    ModelAndView viewArticle(@RequestParam("articleNO") long  articleNO,
                             HttpServletRequest request, HttpServletResponse response) throws Exception;

    /** 글 수정 (단일 이미지 교체) */
    ModelAndView modArticle(MultipartHttpServletRequest multipartRequest,
                                      HttpServletResponse response) throws Exception;

    /** 글 삭제 */
    ModelAndView removeArticle(@RequestParam("articleNO") long  articleNO,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception;

    /** 글쓰기 폼 */
    ModelAndView articleForm() throws Exception;

    /** 답글 폼 */
    ModelAndView replyForm() throws Exception;
}
