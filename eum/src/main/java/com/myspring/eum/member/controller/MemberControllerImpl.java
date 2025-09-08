package com.myspring.eum.member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.myspring.eum.member.service.MemberService;
import com.myspring.eum.member.vo.MemberVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("memberController")
@RequestMapping("/member")
public class MemberControllerImpl implements MemberController {

    @Autowired
    private MemberService memberService;

    /** 회원가입 폼 */
    @RequestMapping(value="/signupForm.do", method=RequestMethod.GET)
    public ModelAndView signupForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("member/signupForm");
    }

    /** 로그인 폼 */
    @RequestMapping(value="/loginForm.do", method=RequestMethod.GET)
    public ModelAndView loginForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("member/loginForm");
    }

    /** 회원가입 처리: POST /member/signup.do */
    @Override
    @RequestMapping(value="/signup.do", method=RequestMethod.POST)
    public ModelAndView signup(@ModelAttribute("info") MemberVO memberVO,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (memberVO == null || isEmpty(memberVO.getId()) || isEmpty(memberVO.getPassword())) {
            return new ModelAndView("redirect:/member/signupForm.do");
        }

        // 중복 체크
        MemberVO exists = memberService.findById(memberVO.getId().trim());
        if (exists != null) {
            return new ModelAndView("redirect:/member/signupForm.do");
        }

        try {
            // 저장 (운영 시 비밀번호 해싱 권장)
            memberVO.setId(memberVO.getId().trim());
            memberService.signup(memberVO); // ⬅️ addMember 대신 signup 사용
        } catch (DuplicateKeyException dke) {
            return new ModelAndView("redirect:/member/signupForm.do");
        }

        return new ModelAndView("redirect:/member/loginForm.do");
    }

    /** 로그인 처리: POST /member/login.do */
    @Override
    @RequestMapping(value="/login.do", method=RequestMethod.POST)
    public ModelAndView login(@ModelAttribute("member") MemberVO member,
                              RedirectAttributes rAttr,
                              HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (member == null || isEmpty(member.getId()) || isEmpty(member.getPassword())) {
            rAttr.addFlashAttribute("loginError", "아이디/비밀번호를 입력하세요.");
            return new ModelAndView("redirect:/member/loginForm.do");
        }

        MemberVO db = memberService.login(member); // mapper: loginById
        if (db == null) {
            rAttr.addFlashAttribute("loginError", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return new ModelAndView("redirect:/member/loginForm.do");
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("member", db);
        session.setMaxInactiveInterval(60 * 60);
        return new ModelAndView("redirect:/");
    }

    /** 로그아웃 */
    @Override
    @RequestMapping(value="/logout.do", method=RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return new ModelAndView("redirect:/");
    }

    private boolean isEmpty(String s) { return s == null || s.trim().length() == 0; }
}
