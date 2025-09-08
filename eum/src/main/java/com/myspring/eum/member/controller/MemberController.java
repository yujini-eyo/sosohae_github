package com.myspring.eum.member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myspring.eum.member.vo.MemberVO;

public interface MemberController {

    /** 회원가입 폼 */
    ModelAndView signupForm(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /** 로그인 폼 */
    ModelAndView loginForm(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /** 회원가입 처리: POST /member/signup.do → 성공 시 /member/loginForm.do */
    ModelAndView signup(@ModelAttribute("info") MemberVO memberVO,
                        HttpServletRequest request, HttpServletResponse response) throws Exception;

    /** 로그인 처리: POST /member/login.do (세션 key: "member") */
    ModelAndView login(@ModelAttribute("member") MemberVO member,
                       RedirectAttributes rAttr,
                       HttpServletRequest request, HttpServletResponse response) throws Exception;

    /** 로그아웃: GET /member/logout.do */
    ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
