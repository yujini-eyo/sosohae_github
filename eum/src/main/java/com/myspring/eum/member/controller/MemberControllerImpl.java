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
//@EnableAspectJAutoProxy
public class MemberControllerImpl implements MemberController {

    @Autowired
    private MemberService memberService;

    @Override
    @RequestMapping(value = "/member/listMembers.do", method = RequestMethod.GET)
    public ModelAndView listMembers(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        // 수정: 올바른 콘텐츠 타입
        response.setContentType("text/html;charset=UTF-8");

        String viewName = (String) request.getAttribute("viewName"); // ex) "/member/listMembers"
        List membersList = memberService.listMembers();
        ModelAndView mav = new ModelAndView(viewName);
        mav.addObject("membersList", membersList);
        return mav;
    }

    @Override
    @RequestMapping(value = "/member/addMember.do", method = RequestMethod.POST)
    public ModelAndView addMember(@ModelAttribute("member") MemberVO member,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        // 수정: 올바른 콘텐츠 타입
        response.setContentType("text/html;charset=UTF-8");

        int result = memberService.addMember(member);
        // 성공/실패 분기 필요하면 result 활용
        return new ModelAndView("redirect:/member/listMembers.do");
    }

    @Override
    @RequestMapping(value = "/member/removeMember.do", method = RequestMethod.GET)
    public ModelAndView removeMember(@RequestParam("id") String id,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        memberService.removeMember(id);
        return new ModelAndView("redirect:/member/listMembers.do");
    }

    // ===== [추가] 명시적 폼 화면 GET 매핑 (Tiles + ViewNameInterceptor 사용 시에도 안전) =====

    @RequestMapping(value = "/member/memberForm.do", method = RequestMethod.GET)
    public ModelAndView memberForm(HttpServletRequest request) throws Exception {
        String viewName = (String) request.getAttribute("viewName"); // -> "/member/memberForm"
        // ViewNameInterceptor가 없다면 아래처럼 명시적으로 써도 됨:
        // String viewName = "/member/memberForm";
        return new ModelAndView(viewName);
    }

    @RequestMapping(value = "/member/loginForm.do", method = RequestMethod.GET)
    public ModelAndView loginForm(@RequestParam(value = "result", required = false) String result,
                                  HttpServletRequest request) throws Exception {
        String viewName = (String) request.getAttribute("viewName"); // -> "/member/loginForm"
        ModelAndView mav = new ModelAndView(viewName);
        mav.addObject("result", result); // 로그인 실패 메시지 전달
        return mav;
    }

    // ===== [기존] 와일드카드 폼 매핑 유지 (명시적 매핑보다 우선순위 낮음) =====
    @RequestMapping(value = "/member/*Form.do", method = RequestMethod.GET)
    private ModelAndView form(@RequestParam(value = "result", required = false) String result,
                              @RequestParam(value = "action", required = false) String action,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
        String viewName = (String) request.getAttribute("viewName");
        HttpSession session = request.getSession();
        session.setAttribute("action", action);
        ModelAndView mav = new ModelAndView();
        mav.addObject("result", result);
        mav.setViewName(viewName);
        return mav;
    }

    @Override
    @RequestMapping(value = "/member/login.do", method = RequestMethod.POST)
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
                mav.setViewName("redirect:/member/listMembers.do");
            }
        } else {
            rAttr.addAttribute("result", "loginFailed");
            mav.setViewName("redirect:/member/loginForm.do");
        }
        return mav;
    }

    @Override
    @RequestMapping(value = "/member/logout.do", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        // 개선: 깔끔하게 세션 종료
        session.invalidate();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/member/listMembers.do");
        return mav;
    }

    // ===== (유지) URI → viewName 유틸 =====
    private String getViewName(HttpServletRequest request) throws Exception {
        String contextPath = request.getContextPath();
        String uri = (String) request.getAttribute("javax.servlet.include.request_uri");
        if (uri == null || uri.trim().equals("")) {
            uri = request.getRequestURI();
        }

        int begin = 0;
        if (!((contextPath == null) || ("".equals(contextPath)))) {
            begin = contextPath.length();
        }

        int end;
        if (uri.indexOf(";") != -1) {
            end = uri.indexOf(";");
        } else if (uri.indexOf("?") != -1) {
            end = uri.indexOf("?");
        } else {
            end = uri.length();
        }

        String viewName = uri.substring(begin, end);
        if (viewName.indexOf(".") != -1) {
            viewName = viewName.substring(0, viewName.lastIndexOf("."));
        }
        if (viewName.lastIndexOf("/") != -1) {
            viewName = viewName.substring(viewName.lastIndexOf("/", 1), viewName.length());
        }
        return viewName;
    }
}
