package com.myspring.eum.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.myspring.eum.auth.service.AuthService;
import com.myspring.eum.admin.vo.AdminVO;
import com.myspring.eum.member.vo.MemberVO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminAuthController")
@RequestMapping("/admin")
public class AdminAuthController {

    @Autowired
    private AuthService authService;

    /** 공통: 관리자 로그인 여부 */
    private boolean isAdminLoggedIn(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s != null && s.getAttribute("adminUser") != null;
    }

    /** 관리자 회원 목록 */
    @RequestMapping(value="/auth/listMembers.do", method=RequestMethod.GET)
    public ModelAndView listMembers(HttpServletRequest request) throws Exception {
        if (!isAdminLoggedIn(request)) return new ModelAndView("redirect:/admin/auth/login.do");
        List<MemberVO> list = authService.listAllMembers();
        ModelAndView mav = new ModelAndView("admin/auth/listMembers");
        mav.addObject("membersList", list);
        return mav;
    }

    /** 관리자 전용: 회원 단건 조회 페이지 (Tiles 정의와 맞춰 'members') */
    @RequestMapping(value="/member/lookup.do", method=RequestMethod.GET)
    public ModelAndView lookupPage(HttpServletRequest request) {
        if (!isAdminLoggedIn(request)) return new ModelAndView("redirect:/admin/auth/login.do");
        return new ModelAndView("admin/member/lookup");
    }

    /** 관리자 메인 */
    @RequestMapping(value="/main.do", method=RequestMethod.GET)
    public ModelAndView adminMain(HttpServletRequest request) {
        if (!isAdminLoggedIn(request)) return new ModelAndView("redirect:/admin/auth/login.do");
        return new ModelAndView("admin/main");
    }

    /** 로그인 폼 */
    @RequestMapping(value="/auth/login.do", method=RequestMethod.GET)
    public ModelAndView loginForm(HttpServletRequest request, HttpServletResponse response) {
        if (isAdminLoggedIn(request)) return new ModelAndView("redirect:/admin/main.do");
        return new ModelAndView("admin/auth/login");
    }

 // ... 생략 ...
    @RequestMapping(value="/auth/login.do", method=RequestMethod.POST)
    public ModelAndView login(
            @RequestParam("id") String id,
            @RequestParam("password") String password,
            HttpServletRequest request,
            RedirectAttributes redirect) throws Exception {

        AdminVO admin = authService.authenticate(id, password);
        if (admin == null) {
            redirect.addFlashAttribute("loginError", "아이디 또는 비밀번호를 확인하세요.");
            return new ModelAndView("redirect:/admin/auth/login.do");
        }

        // ### [추가] 세션 고정 공격 방지: 기존 세션 무효화 후 새 세션 발급
        HttpSession old = request.getSession(false);
        if (old != null) old.invalidate();
        HttpSession session = request.getSession(true);

        session.setAttribute("adminUser", admin);
        session.setAttribute("userRole", "ADMIN");
        session.setMaxInactiveInterval(30 * 60); // 30분

        // 혹시 일반 회원 세션이 섞여있다면 제거
        session.removeAttribute("member");

        return new ModelAndView("redirect:/admin/main.do");
    }

    // ### [개선] 로그아웃: /admin/logout.do 와 /admin/auth/logout.do 둘 다 허용
    @RequestMapping(
        value = { "/logout.do", "/auth/logout.do" },
        method = { RequestMethod.GET, RequestMethod.POST },
        produces = "text/html; charset=UTF-8"
    )
    @ResponseBody
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        HttpHeaders headers = new HttpHeaders(); // Java 6 OK
        headers.add("Content-Type", "text/html; charset=UTF-8");

        String loginUrl = request.getContextPath() + "/admin/auth/login.do";
        String body = "<script>alert('로그아웃 되었습니다.'); location.href='" + loginUrl + "';</script>";

        return new ResponseEntity<String>(body, headers, HttpStatus.OK);
    }


    /** (관리자 전용) 회원 단건 조회 API(JSON) */
    @RequestMapping(value="/api/member/{id}", method=RequestMethod.GET, produces="application/json; charset=UTF-8")
    @ResponseBody
    public ResponseEntity<?> getMember(@PathVariable("id") String id, HttpServletRequest request) {
        if (!isAdminLoggedIn(request)) {
            return new ResponseEntity<String>("관리자 인증이 필요합니다.", HttpStatus.FORBIDDEN);
        }
        try {
            MemberVO m = authService.loadMember(id);
            if (m == null) {
                return new ResponseEntity<String>("회원 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<MemberVO>(m, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("조회 중 오류", HttpStatus.BAD_REQUEST);
        }
    }

    /** 공통 예외 처리 */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<String> handleAny(HttpServletRequest request, Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<String>("요청 처리 중 오류가 발생했습니다.", HttpStatus.BAD_REQUEST);
    }
}
