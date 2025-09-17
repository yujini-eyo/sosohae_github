package com.myspring.eum.support.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.web.servlet.ModelAndView;


public interface SupportController {
ModelAndView applicants(HttpServletRequest request, HttpServletResponse response) throws Exception;
ModelAndView apply(HttpServletRequest request, HttpServletResponse response) throws Exception;
ModelAndView cancel(HttpServletRequest request, HttpServletResponse response) throws Exception;
ModelAndView select(HttpServletRequest request, HttpServletResponse response) throws Exception;
ModelAndView myApplications(HttpServletRequest request, HttpServletResponse response) throws Exception;
ModelAndView myRequests(HttpServletRequest request, HttpServletResponse response) throws Exception;
}