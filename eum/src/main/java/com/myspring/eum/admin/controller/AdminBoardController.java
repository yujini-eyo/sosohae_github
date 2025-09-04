package com.myspring.eum.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public interface AdminBoardController {

    ModelAndView listArticles(HttpServletRequest request,
                              HttpServletResponse response) throws Exception;

    ResponseEntity<String> addNewArticle(MultipartHttpServletRequest multipartRequest,
                                         HttpServletResponse response) throws Exception;

    ModelAndView viewArticle(@RequestParam("articleNO") int articleNO,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception;

    ResponseEntity<String> removeArticle(@RequestParam("articleNO") int articleNO,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws Exception;
}
