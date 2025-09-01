package com.myspring.eum.main.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller("mainController")
public class MainControllerImpl implements MainController {

    /** 루트 & 메인 */
    @Override
    @RequestMapping(value = { "/", "/main.do" }, method = RequestMethod.GET)
    public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Tiles에 name="main" 정의가 있어야 합니다.
        return new ModelAndView("main");
    }

    /** 상단 공용 페이지들 (필요에 맞게 추가/삭제) */
    @Override
    @RequestMapping(value = { "/index.do", "/home.do", "/intro.do", "/about.do", "/contact.do" },
                    method = RequestMethod.GET)
    public ModelAndView pages(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String viewName = (String) request.getAttribute("viewName");
        if (viewName == null || viewName.trim().isEmpty()) {
            viewName = getViewName(request);
        }
        return new ModelAndView(viewName);
    }

    /** 루트 레벨 폼 (예: /loginForm.do, /joinForm.do) */
    @Override
    @RequestMapping(value = "/*Form.do", method = RequestMethod.GET)
    public ModelAndView form(@RequestParam(value = "result", required = false) String result,
                             @RequestParam(value = "action", required = false) String action,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {

        String viewName = (String) request.getAttribute("viewName");
        if (viewName == null || viewName.trim().isEmpty()) {
            viewName = getViewName(request);
        }

        HttpSession session = request.getSession();
        if (action != null && !action.isEmpty()) {
            session.setAttribute("action", action);
        }

        ModelAndView mav = new ModelAndView(viewName);
        mav.addObject("result", result);
        return mav;
    }

    /** URI -> viewName 변환 (선행 슬래시/확장자 제거) */
    private String getViewName(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String uri = (String) request.getAttribute("javax.servlet.include.request_uri");
        if (uri == null || uri.trim().isEmpty()) {
            uri = request.getRequestURI();
        }

        int begin = (contextPath == null || contextPath.isEmpty()) ? 0 : contextPath.length();
        int end = uri.indexOf(';') != -1 ? uri.indexOf(';')
                : (uri.indexOf('?') != -1 ? uri.indexOf('?') : uri.length());

        String path = uri.substring(begin, end);      // e.g. "/loginForm.do"
        if (path.startsWith("/")) path = path.substring(1); // "loginForm.do"

        int dot = path.lastIndexOf('.');
        if (dot >= 0) path = path.substring(0, dot);  // "loginForm"

        int slash = path.lastIndexOf('/');
        if (slash >= 0) path = path.substring(slash + 1);

        return path; // e.g. "loginForm"
    }
}
