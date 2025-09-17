package com.myspring.eum.support.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myspring.eum.member.vo.MemberVO;
import com.myspring.eum.support.service.SupportService;
import com.myspring.eum.support.vo.SupportApplicationVO;

@Controller("supportController")
@RequestMapping("/support")
public class SupportControllerImpl implements SupportController {

	@Autowired
	private SupportService supportService;

	private String currentUserId(HttpSession session) {
		MemberVO member = (session != null) ? (MemberVO) session.getAttribute("member") : null;
		return (member != null && member.getId() != null) ? member.getId() : null;
	}

	/** 지원자 목록 (요청자 전용) - 파라미터 없으면 JSP만 포워드 */
	@Override
	@RequestMapping(value = "/applicants.do", method = RequestMethod.GET)
	public ModelAndView applicants(HttpServletRequest request, HttpServletResponse response) throws Exception {

	    String articleNoParam = request.getParameter("articleNO");

	    // 1) 파라미터 없거나 공백이면: JSP만 포워드(서비스 미호출)
	    if (articleNoParam == null || articleNoParam.trim().isEmpty()) {
	        // Tiles 안 쓰고 JSP만 확인
	        //return new ModelAndView("forward:/WEB-INF/views/support/applicants.jsp");

	        // 만약 Tiles 정의를 테스트하고 싶다면 위 한 줄 대신 아래 한 줄:
	         return new ModelAndView("support/applicants");
	    }

	    // 2) 파라미터가 있으면 정상 처리
	    int articleNo = Integer.parseInt(articleNoParam);
	    String requesterId = currentUserId(request.getSession());

	    ModelAndView mv = new ModelAndView("support/applicants"); // Tiles 정의명
	    mv.addObject("applicants", supportService.listApplicants(articleNo, requesterId));
	    mv.addObject("articleNO", articleNo);
	    return mv;
	}


	/** 지원 신청 */
	@Override
	@RequestMapping(value = "/apply.do", method = RequestMethod.POST)
	public ModelAndView apply(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int articleNo = Integer.parseInt(request.getParameter("articleNO"));
		String message = request.getParameter("message");
		String uid = currentUserId(request.getSession());
		supportService.apply(articleNo, uid, message);
		return new ModelAndView("redirect:/board/viewArticle.do?articleNO=" + articleNo);
	}

	/** 신청 취소 */
	@Override
	@RequestMapping(value = "/cancel.do", method = RequestMethod.POST)
	public ModelAndView cancel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long applicationId = Long.parseLong(request.getParameter("applicationId"));
		String uid = currentUserId(request.getSession());
		supportService.cancel(applicationId, uid);
		String redirect = request.getParameter("redirect");
		if (redirect != null && redirect.startsWith("/")) {
			return new ModelAndView("redirect:" + redirect);
		}
		int articleNo = Integer.parseInt(request.getParameter("articleNO"));
		return new ModelAndView("redirect:/board/viewArticle.do?articleNO=" + articleNo);
	}

	/** 지원자 선정 (요청자 전용) */
	@Override
	@RequestMapping(value = "/select.do", method = RequestMethod.POST)
	public ModelAndView select(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long applicationId = Long.parseLong(request.getParameter("applicationId"));
		String requesterId = currentUserId(request.getSession());
		supportService.select(applicationId, requesterId);
		int articleNo = Integer.parseInt(request.getParameter("articleNO"));
		return new ModelAndView("redirect:/support/applicants.do?articleNO=" + articleNo);
	}

	@Override
	@RequestMapping(value = "/myApplications.do", method = RequestMethod.GET)
	public ModelAndView myApplications(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    ModelAndView mv = new ModelAndView("support/myApplications");

	    try {
	        String volunteerId = currentUserId(request.getSession());
	        List<SupportApplicationVO> myApps = supportService.listMyApplications(volunteerId); // 아직 미구현이어도 OK
	        mv.addObject("applications", myApps);
	        mv.addObject("debug", "service OK (size=" + (myApps != null ? myApps.size() : 0) + ")");
	    } catch (Exception e) {
	        // 서비스/매퍼 미구현이어도 JSP는 열리도록 빈 리스트 + 디버그 메시지 전달
	        mv.addObject("applications", Collections.emptyList());
	        mv.addObject("debug", "service ERROR: " + e.getClass().getSimpleName());
	    }

	    return mv;
	}

	@Override
	@RequestMapping(value = "/myRequests.do", method = RequestMethod.GET)
	public ModelAndView myRequests(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    String requesterId = currentUserId(request.getSession());
	    // 서비스에서 요청 글 목록을 가져오는 메서드를 호출해야 합니다.
	    // List<BoardArticleVO> myReqs = supportService.listMyRequests(requesterId); 

	    ModelAndView mv = new ModelAndView("support/myRequests");
	    // mv.addObject("requests", myReqs);

	    return mv;
	}
}