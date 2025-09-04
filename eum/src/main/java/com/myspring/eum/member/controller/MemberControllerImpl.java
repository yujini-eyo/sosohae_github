package com.myspring.eum.member.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myspring.eum.member.service.MemberService;
import com.myspring.eum.member.vo.MemberVO;

@Controller("memberController")
@RequestMapping("/eum") // ✅ /eum/... 로 공개 URL 통일
public class MemberControllerImpl implements MemberController {

    @Autowired
    private MemberService memberService;

    // ===== 목록 =====
    @Override
    @RequestMapping(value = "/listMembers.do", method = RequestMethod.GET)
    public ModelAndView listMembers(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");

        List membersList = memberService.listMembers();
        ModelAndView mav = new ModelAndView("listMembers"); // ✅ Tiles 이름 짧게
        mav.addObject("membersList", membersList);
        return mav;
    }

    // ===== 회원가입 화면 =====
    @RequestMapping(value = "/signupForm.do", method = RequestMethod.GET)
    public String signupForm() {
        return "signupForm"; // ✅ Tiles 정의 이름과 동일
    }

    // ===== 로그인 화면 =====
    @RequestMapping(value = "/loginForm.do", method = RequestMethod.GET)
    public ModelAndView loginForm(@RequestParam(value = "result", required = false) String result) {
        ModelAndView mav = new ModelAndView("loginForm"); // ✅ Tiles 정의 이름과 동일
        mav.addObject("result", result);
        return mav;
    }

    // ===== 회원가입 처리 =====
    @Override
    @RequestMapping(value = "/addMember.do", method = RequestMethod.POST)
    public ModelAndView addMember(@ModelAttribute("member") MemberVO member,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");

        int result = memberService.addMember(member);
        // 필요 시 result로 분기 가능
        return new ModelAndView("redirect:/eum/listMembers.do");
    }

    // ===== 회원 삭제 =====
    @Override
    @RequestMapping(value = "/removeMember.do", method = RequestMethod.GET)
    public ModelAndView removeMember(@RequestParam("id") String id,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        memberService.removeMember(id);
        return new ModelAndView("redirect:/eum/listMembers.do");
    }

    // ===== 로그인 처리 =====
    @Override
    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    public ModelAndView login(@ModelAttribute("member") MemberVO member,
                              RedirectAttributes rAttr,
                              HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();

        MemberVO loginUser = memberService.login(member);
        if (loginUser != null) {
            HttpSession session = request.getSession();
            session.setAttribute("member", loginUser);
            session.setAttribute("isLogOn", true);

            String action = (String) session.getAttribute("action");
            session.removeAttribute("action");
            if (action != null) {
                mav.setViewName("redirect:" + action);
            } else {
                mav.setViewName("redirect:/main.do");
            }
        } else {
            rAttr.addAttribute("result", "loginFailed");
            mav.setViewName("redirect:/eum/loginForm.do");
        }
        return mav;
    }

    // ===== 로그아웃 =====
    @Override
    @RequestMapping(value = "/logout.do", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        session.invalidate(); // ✅ 깔끔 종료
        return new ModelAndView("redirect:/eum/listMembers.do");
    }
}
