package com.myspring.eum.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public interface BoardAdminController {
    ModelAndView adminList(HttpServletRequest request, HttpServletResponse response) throws Exception;
    ModelAndView adminview(HttpServletRequest request, HttpServletResponse response, int articleNO) throws Exception;
    ModelAndView adminWriteForm(HttpServletRequest request, HttpServletResponse response) throws Exception;
    ModelAndView adminaddNewArticle(HttpServletRequest request, HttpServletResponse response) throws Exception;
    ModelAndView adminremoveArticle(HttpServletRequest request, HttpServletResponse response, int articleNO) throws Exception;
    ModelAndView adminnoticeOn(HttpServletRequest request, HttpServletResponse response, int articleNO) throws Exception;
    ModelAndView adminnoticeOff(HttpServletRequest request, HttpServletResponse response, int articleNO) throws Exception;
}
