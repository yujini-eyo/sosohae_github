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

@Controller("adminAuthController")
@RequestMapping("/admin")
public class AdminAuthController {

    @Autowired
    private AuthService authService;

    /** 관리자 회원 목록 */
    @RequestMapping(value="/auth/listMembers.do", method=RequestMethod.GET)
    public ModelAndView listMembers(HttpServletRequest request) throws Exception {
        HttpSession s = request.getSession(false);
        if (s == null || s.getAttribute("adminUser") == null) {
            return new ModelAndView("redirect:/admin/auth/login.do");
        }
        List<MemberVO> list = authService.listAllMembers(); // AuthService에 메서드 선언/구현 필요
        ModelAndView mav = new ModelAndView("admin/auth/listMembers");
        mav.addObject("membersList", list);
        return mav;
    }

    /** 관리자 전용: 회원 단건 조회 페이지 */
    @RequestMapping(value="/members/lookup.do", method=RequestMethod.GET)
    public ModelAndView lookupPage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
            return new ModelAndView("redirect:/admin/auth/login.do");
        }
        // ✅ 여기! 'members' → 'member' (단수)
        return new ModelAndView("admin/member/lookup"); // /WEB-INF/views/admin/member/lookup.jsp
    }

    /** 관리자 메인 */
    @RequestMapping(value="/main.do", method=RequestMethod.GET)
    public ModelAndView adminMain(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
            return new ModelAndView("redirect:/admin/auth/login.do");
        }
        return new ModelAndView("admin/main");
    }

    /** 로그인 폼 */
    @RequestMapping(value="/auth/login.do", method=RequestMethod.GET)
    public ModelAndView loginForm(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("admin/auth/login");
    }

    /** 로그인 처리 */
    @RequestMapping(value="/auth/login.do", method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> login(
            @RequestParam("id") String id,
            @RequestParam("password") String password,
            HttpServletRequest request) {
        try {
            AdminVO admin = authService.authenticate(id, password);

            HttpHeaders h = new HttpHeaders();
            h.add("Content-Type", "text/html; charset=UTF-8");

            if (admin == null) {
                String msg = "<script>alert('아이디 또는 비밀번호를 확인하세요.'); history.back();</script>";
                return new ResponseEntity<String>(msg, h, HttpStatus.OK);
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("adminUser", admin);
            session.setMaxInactiveInterval(60 * 60);

            String msg = "<script>alert('관리자 로그인 성공'); location.href='"
                       + request.getContextPath()
                       + "/admin/main.do';</script>";
            return new ResponseEntity<String>(msg, h, HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
            HttpHeaders h = new HttpHeaders();
            h.add("Content-Type", "text/html; charset=UTF-8");
            String msg = "<script>alert('로그인 처리 중 오류가 발생했습니다.'); history.back();</script>";
            return new ResponseEntity<String>(msg, h, HttpStatus.BAD_REQUEST);
        }
    }

    /** 로그아웃 */
    @RequestMapping(value="/logout.do", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        HttpHeaders h = new HttpHeaders();
        h.add("Content-Type", "text/html; charset=UTF-8");
        String msg = "<script>alert('로그아웃 되었습니다.'); location.href='"
                   + request.getContextPath()
                   + "/admin/auth/login.do';</script>";
        return new ResponseEntity<String>(msg, h, HttpStatus.OK);
    }

    /** (관리자 전용) 회원 단건 조회 API(JSON) */
    @RequestMapping(value="/api/members/{id}", method=RequestMethod.GET, produces="application/json; charset=UTF-8")
    @ResponseBody
    public ResponseEntity<?> getMember(@PathVariable("id") String id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
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
        HttpHeaders h = new HttpHeaders();
        h.add("Content-Type", "text/html; charset=UTF-8");
        String msg = "<script>alert('요청 처리 중 오류가 발생했습니다.'); history.back();</script>";
        return new ResponseEntity<String>(msg, h, HttpStatus.BAD_REQUEST);
    }
}
