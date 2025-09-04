package com.myspring.eum.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.myspring.eum.admin.service.AdminBoardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller("adminBoardController")
@RequestMapping("/admin/board")
public class AdminBoardControllerImpl {

    @Autowired
    private AdminBoardService adminBoardService;

    /** 컨트롤러 레벨 관리자 가드 */
    private void assertAdmin(HttpSession session){
        Object role = (session == null) ? null : session.getAttribute("userRole");
        boolean ok = (role instanceof String) && "ADMIN".equalsIgnoreCase((String) role);
        if (!ok) throw new SecurityException("FORBIDDEN: admin only");
    }

    /** 목록 */
    @RequestMapping(value = "/listArticles.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView listArticles(HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
        assertAdmin(request.getSession(false));
        return adminBoardService.listArticles(request, response);
    }

    /** ✅ 등록(저장) — POST : 서비스로 위임만 */
    @RequestMapping(value = "/addNewArticle.do", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addNewArticle(MultipartHttpServletRequest req,
                                                HttpServletResponse res) throws Exception {
        assertAdmin(req.getSession(false));
        return adminBoardService.addNewArticle(req, res);
    }

    /** 상세 — GET */
    @RequestMapping(value = "/viewArticle.do", method = RequestMethod.GET)
    public ModelAndView viewArticle(@RequestParam(value = "articleNO", required = false) Integer articleNO,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        assertAdmin(request.getSession(false));
        if (articleNO == null) {
            return new ModelAndView("redirect:/admin/board/listArticles.do");
        }
        return adminBoardService.viewArticle(articleNO, request, response);
    }

    /** 삭제 — POST */
    @RequestMapping(value = "/removeArticle.do", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> removeArticle(@RequestParam(value = "articleNO", required = false) Integer articleNO,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws Exception {
        assertAdmin(request.getSession(false));
        if (articleNO == null) {
            HttpHeaders h = new HttpHeaders();
            h.add("Content-Type", "text/html; charset=UTF-8");
            String msg = "<script>alert('잘못된 요청입니다. (articleNO)'); history.back();</script>";
            return new ResponseEntity<String>(msg, h, HttpStatus.BAD_REQUEST); // ◀ 제네릭 명시
        }
        return adminBoardService.removeArticle(articleNO, request, response);
    }

    /** 글쓰기 폼 — GET */
    @RequestMapping(value = "/writeForm.do", method = RequestMethod.GET)
    public ModelAndView writeForm(HttpServletRequest request, HttpServletResponse response) {
        assertAdmin(request.getSession(false));
        return new ModelAndView("admin/board/writeForm");
    }

    /** 폼 라우팅 */
    @RequestMapping(value = "/*Form.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView form(HttpServletRequest request, HttpServletResponse response) {
        assertAdmin(request.getSession(false));
        String viewName = (String) request.getAttribute("viewName");
        if (viewName == null || viewName.trim().isEmpty()) viewName = "admin/board/writeForm";
        return new ModelAndView(viewName);
    }

    /* ===== 예외 처리 ===== */
    @ExceptionHandler(SecurityException.class)
    @ResponseBody
    public ResponseEntity<String> handleSecurityException(HttpServletRequest request, SecurityException ex) {
        HttpHeaders h = new HttpHeaders();
        h.add("Content-Type", "text/html; charset=UTF-8");
        String msg = "<script>alert('관리자만 접근할 수 있습니다.');"
                   + "location.href='" + request.getContextPath() + "/admin/login.do';</script>";
        return new ResponseEntity<String>(msg, h, HttpStatus.FORBIDDEN); // ◀ 제네릭 명시
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseEntity<String> handleMissingParam(HttpServletRequest request, MissingServletRequestParameterException ex) {
        HttpHeaders h = new HttpHeaders();
        h.add("Content-Type", "text/html; charset=UTF-8");
        String msg = "<script>alert('잘못된 요청입니다. (" + ex.getParameterName() + ")'); history.back();</script>";
        return new ResponseEntity<String>(msg, h, HttpStatus.BAD_REQUEST); // ◀ 제네릭 명시
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseEntity<String> handleMethodNotAllowed(HttpServletRequest request, HttpRequestMethodNotSupportedException ex) {
        HttpHeaders h = new HttpHeaders();
        h.add("Content-Type", "text/html; charset=UTF-8");
        String msg = "<script>alert('허용되지 않은 요청 방식입니다.'); history.back();</script>";
        return new ResponseEntity<String>(msg, h, HttpStatus.METHOD_NOT_ALLOWED); // ◀ 제네릭 명시
    }
}
