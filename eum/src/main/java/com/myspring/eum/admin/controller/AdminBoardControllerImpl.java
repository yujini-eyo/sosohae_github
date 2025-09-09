package com.myspring.eum.admin.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.myspring.eum.admin.service.AdminBoardService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller("adminBoardController")
@RequestMapping("/admin/board")
public class AdminBoardControllerImpl {

    private static final Logger log = LoggerFactory.getLogger(AdminBoardControllerImpl.class);

    @Autowired
    private AdminBoardService adminBoardService;

    /** 컨트롤러 레벨 관리자 가드 (adminUser가 있거나 userRole==ADMIN이면 통과) */
    private void assertAdmin(HttpSession session){
        if (session == null) {
            log.warn("[ADMIN GUARD] session is null");
            throw new SecurityException("FORBIDDEN: admin only");
        }
        if (session.getAttribute("adminUser") != null) {
            return;
        }
        Object role = session.getAttribute("userRole");
        if (role instanceof String && "ADMIN".equalsIgnoreCase((String) role)) {
            return;
        }
        log.warn("[ADMIN GUARD] not admin. userRole={}", role);
        throw new SecurityException("FORBIDDEN: admin only");
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
    @RequestMapping(
        value = "/addNewArticle.do",
        method = RequestMethod.POST,
        consumes = "multipart/form-data"
    )
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

        String ctx = safeCtx(req);
        String to  = noticeFlag ? (ctx + "/notice.do")
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
            log.info("[viewArticle] articleNO is null -> redirect list");
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
        sendScript(response, "삭제되었습니다.", safeCtx(request) + "/admin/board/listArticles.do");
        return null;
    }

    /** 글쓰기 폼 — GET */
    @RequestMapping(value = "/writeForm.do", method = RequestMethod.GET)
    public ModelAndView writeForm(HttpServletRequest request, HttpServletResponse response) {
        assertAdmin(request.getSession(false));
        return new ModelAndView("admin/board/writeForm");
    }

    /** 폼 라우팅 (공통) */
    @RequestMapping(value = "/*Form.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView form(HttpServletRequest request, HttpServletResponse response) {
        assertAdmin(request.getSession(false));
        String viewName = (String) request.getAttribute("viewName");
        if (viewName == null || viewName.trim().isEmpty()) viewName = "admin/board/writeForm";
        return new ModelAndView(viewName);
    }

    /* ================= 예외 처리 ================= */

    @ExceptionHandler(SecurityException.class)
    public void handleSecurityException(HttpServletRequest request, HttpServletResponse response, SecurityException ex) throws Exception {
        log.warn("[SECURITY] {}", ex.getMessage());
        sendScript(response, "관리자만 접근할 수 있습니다.", safeCtx(request) + "/admin/auth/login.do");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public void handleMissingParam(HttpServletResponse response, MissingServletRequestParameterException ex) throws Exception {
        log.info("[BAD REQUEST] missing param: {}", ex.getParameterName());
        sendScript(response, "잘못된 요청입니다. (" + ex.getParameterName() + ")", "javascript:history.back();");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public void handleMethodNotAllowed(HttpServletResponse response, HttpRequestMethodNotSupportedException ex) throws Exception {
        log.info("[METHOD NOT ALLOWED] {}", ex.getMethod());
        sendScript(response, "허용되지 않은 요청 방식입니다.", "javascript:history.back();");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public void handleMaxUpload(HttpServletResponse response, MaxUploadSizeExceededException ex) throws Exception {
        log.info("[UPLOAD EXCEEDED] {}", ex.getMaxUploadSize());
        sendScript(response, "업로드 용량을 초과했습니다.", "javascript:history.back();");
    }

    @ExceptionHandler(Exception.class)
    public void handleGeneric(HttpServletResponse response, Exception ex) throws Exception {
        log.error("[ERROR] unhandled", ex);
        sendScript(response, "처리 중 오류가 발생했습니다.", "javascript:history.back();");
    }

    /* ================= 바인딩/유틸 ================= */

    /** 폼 입력 공백 → null/trim 처리 (전역 바인더) */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // true = 빈 문자열을 null로 변환
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    private static boolean isChecked(String v){
        return v != null && (
               "1".equals(v) ||
               "on".equalsIgnoreCase(v) ||
               "true".equalsIgnoreCase(v));
    }

    private static String safeCtx(HttpServletRequest req){
        return (req != null && req.getContextPath() != null) ? req.getContextPath() : "";
    }

    private static void sendScript(HttpServletResponse response, String msg, String to) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.append("<!DOCTYPE html><html><head><meta charset='UTF-8'>")
           .append("<meta http-equiv='x-ua-compatible' content='IE=edge'>")
           .append("</head><body>")
           .append("<script>")
           .append("alert('").append(escapeJs(msg)).append("');");
        if (to != null && to.startsWith("javascript:")) {
            out.append(to.substring("javascript:".length()));
        } else if (to != null && !to.isEmpty()) {
            out.append("location.href='").append(escapeJs(to)).append("';");
        } else {
            out.append("history.back();");
        }
        out.append("</script></body></html>");
        out.flush();
    }

    /** JS 문자열 안전화: 스크립트 파손/간단 XSS 방지 */
    private static String escapeJs(String s){
        if (s == null) return "";
        String r = s;
        r = r.replace("\\", "\\\\");
        r = r.replace("'", "\\'");
        r = r.replace("\n","\\n").replace("\r","\\r");
        // 태그/슬래시도 이스케이프 (</script> 파손 방지)
        r = r.replace("<", "\\x3C").replace(">", "\\x3E").replace("/", "\\/");
        return r;
    }
}
