package com.myspring.eum.admin.controller;

import javax.servlet.http.*;

import com.myspring.eum.auth.service.AuthService;
import com.myspring.eum.member.vo.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller("adminAuthController")
@RequestMapping("/admin")
public class AdminAuthController {

    @Autowired
    private AuthService authService;

    /** 로그인 폼 (Tiles 정의명 또는 JSP 뷰명) */
    @RequestMapping(value = "/login.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView loginForm(HttpServletRequest request, HttpServletResponse response) {
        // Tiles 정의명: admin/auth/login  (앞에 / 붙이지 않기)
        return new ModelAndView("admin/auth/login");
    }

    /** 로그인 처리 */
    @RequestMapping(value = "/doLogin.do", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> doLogin(@RequestParam("id") String id,
                                          @RequestParam("password") String pw,
                                          HttpServletRequest request) throws Exception {
        HttpHeaders h = new HttpHeaders();
        h.add("Content-Type", "text/html; charset=UTF-8");

        // 1) 계정 검증
        MemberVO user = authService.authenticate(id, pw);
        if (user == null) {
            String msg = "<script>alert('아이디 또는 비밀번호가 올바르지 않습니다.'); history.back();</script>";
            return new ResponseEntity<String>(msg, h, HttpStatus.UNAUTHORIZED);
        }

        // 2) 관리자 권한 확인 (role 사용)
        final String role = user.getRole();
        if (role == null || !"ADMIN".equalsIgnoreCase(role)) {
            String msg = "<script>alert('관리자 권한이 없습니다.'); history.back();</script>";
            return new ResponseEntity<String>(msg, h, HttpStatus.FORBIDDEN);
        }

        // 3) 세션 저장
        HttpSession session = request.getSession(true);
        session.setAttribute("userId",   user.getId());
        session.setAttribute("userRole", role);
        session.setAttribute("member",   user);

        // 4) 성공 이동
        String msg = "<script>"
                   + "alert('관리자 로그인 되었습니다.');"
                   + "location.href='" + request.getContextPath() + "/admin/board/listArticles.do';"
                   + "</script>";
        return new ResponseEntity<String>(msg, h, HttpStatus.OK);
    }

    /** 로그아웃 */
    @RequestMapping(value = "/logout.do", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();

        HttpHeaders h = new HttpHeaders();
        h.add("Content-Type", "text/html; charset=UTF-8");
        String msg = "<script>"
                   + "alert('로그아웃 되었습니다.');"
                   + "location.href='" + request.getContextPath() + "/admin/login.do';"
                   + "</script>";
        return new ResponseEntity<String>(msg, h, HttpStatus.OK);
    }

    /** 예외 공통 처리(선택) */
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
