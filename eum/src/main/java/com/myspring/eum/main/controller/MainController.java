package com.myspring.eum.main.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myspring.eum.main.vo.MainVO;


/**
 * MainController interface - Implemented by MainControllerImpl - Keep
 * signatures identical to the implementation.
 */
public interface MainController {

	/** GET: "/", "/main.do" */
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception;

	/**
	 * GET:  "/guide.do", "/intro.do", "/about.do", "/contact.do" (extend
	 * as needed)
	 */
	public ModelAndView pages(HttpServletRequest request, HttpServletResponse response) throws Exception;

	/** GET: "/*Form.do" at root level (e.g., "/loginForm.do", "/joinForm.do") */
	public ModelAndView form(@RequestParam(value = "result", required = false) String result,
			@RequestParam(value = "action", required = false) String action, HttpServletRequest request,
			HttpServletResponse response) throws Exception;
}
