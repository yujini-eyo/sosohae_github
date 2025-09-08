package com.myspring.eum.admin.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.myspring.eum.admin.service.AdminBoardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller("adminBoardController")
@RequestMapping("/admin/board")
public class AdminBoardControllerImpl {

    @Autowired
    private AdminBoardService adminBoardService;

    /** 컨트롤러 레벨 관리자 가드 (session.userRole == ADMIN 이면 통과) */
    private void assertAdmin(HttpSession session){
        boolean ok = false;
        if (session != null) {
            Object role = session.getAttribute("userRole");
            if (role instanceof String && "ADMIN".equalsIgnoreCase((String) role)) {
                ok = true;
            }
            // 프로젝트 정책상 member.role도 허용하려면 아래 주석 해제
            // else {
            //     Object member = session.getAttribute("member");
            //     if (member != null) {
            //         try {
            //             String mrole = (String) member.getClass().getMethod("getRole").invoke(member);
            //             ok = "ADMIN".equalsIgnoreCase(mrole);
            //         } catch (Exception ignore) {}
            //     }
            // }
        }
        if (!ok) throw new SecurityException("FORBIDDEN: admin only");
    }

    /** 루트 접근 시 목록으로 */
    @RequestMapping(value = {"", "/", "/index.do"}, method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("redirect:/admin/board/listArticles.do");
    }

    /** 목록 */
    @RequestMapping(value = "/listArticles.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView listArticles(HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
        assertAdmin(request.getSession(false));
        return adminBoardService.listArticles(request, response);
    }

    /** 등록(저장) — GET 접근 시 폼으로 유도 */
    @RequestMapping(value = "/addNewArticle.do", method = RequestMethod.GET)
    public ModelAndView addNewArticleGet(HttpServletRequest req, HttpServletResponse res) throws Exception {
        assertAdmin(req.getSession(false));
        return new ModelAndView("redirect:/admin/board/writeForm.do");
    }

    /** 등록(저장) — POST : 공지 여부에 따라 리다이렉트 분기 (스크립트 응답) */
    @RequestMapping(value = "/addNewArticle.do", method = RequestMethod.POST)
    public ModelAndView addNewArticle(MultipartHttpServletRequest req,
                                      HttpServletResponse res) throws Exception {
        assertAdmin(req.getSession(false));

        // 체크박스 값: "1" | "on" | "true" 허용
        boolean noticeFlag = isChecked(req.getParameter("isNotice"));

        // 서비스 저장 위임 (서비스 내부에서 DB insert/파일저장)
        adminBoardService.addNewArticle(req, res);

        // 서비스에서 이미 응답/리다이렉트 했다면 더 쓰지 않음 (이중 커밋 방지)
        if (res.isCommitted()) {
            return null;
        }

        String ctx = req.getContextPath();
        String to  = noticeFlag ? (ctx + "/eum/notice.do")
                                : (ctx + "/admin/board/listArticles.do");
        sendScript(res, "등록되었습니다.", to);
        return null; // 스크립트로 응답 완료
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
    public ModelAndView removeArticle(@RequestParam(value = "articleNO", required = false) Integer articleNO,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        assertAdmin(request.getSession(false));
        if (articleNO == null) {
            sendScript(response, "잘못된 요청입니다. (articleNO)", "javascript:history.back();");
            return null;
        }
        adminBoardService.removeArticle(articleNO, request, response);

        if (response.isCommitted()) {
            return null;
        }
        sendScript(response, "삭제되었습니다.", request.getContextPath() + "/admin/board/listArticles.do");
        return null;
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

    /* ================= 예외 처리(보수적: 스크립트 응답) ================= */

    @ExceptionHandler(SecurityException.class)
    public void handleSecurityException(HttpServletRequest request, HttpServletResponse response, SecurityException ex) throws Exception {
        sendScript(response, "관리자만 접근할 수 있습니다.", request.getContextPath() + "/admin/auth/login.do");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public void handleMissingParam(HttpServletResponse response, MissingServletRequestParameterException ex) throws Exception {
        sendScript(response, "잘못된 요청입니다. (" + ex.getParameterName() + ")", "javascript:history.back();");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public void handleMethodNotAllowed(HttpServletResponse response, HttpRequestMethodNotSupportedException ex) throws Exception {
        sendScript(response, "허용되지 않은 요청 방식입니다.", "javascript:history.back();");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public void handleMaxUpload(HttpServletResponse response, MaxUploadSizeExceededException ex) throws Exception {
        sendScript(response, "업로드 용량을 초과했습니다.", "javascript:history.back();");
    }

    @ExceptionHandler(Exception.class)
    public void handleGeneric(HttpServletResponse response, Exception ex) throws Exception {
        sendScript(response, "처리 중 오류가 발생했습니다.", "javascript:history.back();");
    }

    /* ================= 공통 유틸 ================= */
    private static boolean isChecked(String v){
        return v != null && (
               "1".equals(v) ||
               "on".equalsIgnoreCase(v) ||
               "true".equalsIgnoreCase(v));
    }

    private static void sendScript(HttpServletResponse response, String msg, String to) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.append("<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>")
           .append("<script>")
           .append("alert('").append(escapeJs(msg)).append("');");
        if (to != null && to.startsWith("javascript:")) {
            out.append(to.substring("javascript:".length()));
        } else if (to != null && to.length() > 0) {
            out.append("location.href='").append(escapeJs(to)).append("';");
        }
        out.append("</script></body></html>");
        out.flush();
    }

    private static String escapeJs(String s){
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("'", "\\'").replace("\n","\\n").replace("\r","\\r");
    }
}
