package com.myspring.eum.support.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.myspring.eum.board.service.BoardService;
import com.myspring.eum.board.vo.ArticleVO;
import com.myspring.eum.member.vo.MemberVO;
import com.myspring.eum.support.service.SupportService;
import com.myspring.eum.support.vo.SupportApplicationVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 지원(Support) 관련 화면 컨트롤러 - /support/applicants.do : 특정 글(articleNO)의 지원자 목록 -
 * /support/myApplicants.do : 내가 올린 모든 글에 달린 지원자 통합 목록 - /support/myRequests.do
 * : 내가 올린 글 목록 - /support/apply.do : 특정 글에 지원(POST)
 *
 * Tiles 뷰 키: "support/applicants", "support/myApplicants", "support/myRequests"
 */
@Controller("supportController")
@RequestMapping("/support")
public class SupportControllerImpl {

	@Autowired
	private SupportService supportService;

	// 선택 의존성: 글 소유자 확인 등에 사용 중이라면 유지, 아니면 제거 가능
	@Autowired(required = false)
	private BoardService boardService;

	/** 세션에서 로그인 사용자 id 조회 (프로젝트 컨벤션에 맞게 MemberVO 사용) */
	private String currentUserId(HttpSession session) {
		if (session == null)
			return null;
		Object m = session.getAttribute("member");
		if (m instanceof MemberVO) {
			String id = ((MemberVO) m).getId();
			return (id != null && !id.isEmpty()) ? id : null;
		}
		return null;
	}

	// =========================================================
	// [POST] 지원하기
	// =========================================================
	@RequestMapping(value = "/apply.do", method = RequestMethod.POST)
	public String apply(@RequestParam("articleNO") int articleNO,
			@RequestParam(value = "message", required = false) String message, HttpServletRequest request,
			RedirectAttributes rttr) {
		String me = currentUserId(request.getSession());
		if (me == null) {
			rttr.addFlashAttribute("msg", "로그인 후 지원할 수 있습니다.");
			return "redirect:/member/loginForm.do";
		}

		try {
			// SupportService에 apply(articleNO, volunteerId, message) 구현 필요
			supportService.apply(articleNO, me, message);
			rttr.addFlashAttribute("msg", "지원이 완료되었습니다.");
		} catch (DuplicateKeyException e) {
			rttr.addFlashAttribute("msg", "이미 지원한 글입니다.");
		} catch (Exception e) {
			rttr.addFlashAttribute("msg", "지원 처리 중 오류가 발생했습니다.");
		}

		return "redirect:/board/viewArticle.do?articleNO=" + articleNO;
	}

	// =========================================================
	// [GET] 특정 글의 지원자 목록 — applicants.jsp
	// URL 예: /support/applicants.do?articleNO=123
	// =========================================================
	@RequestMapping(value = "/applicants.do", method = RequestMethod.GET)
	public ModelAndView applicants(@RequestParam(value = "articleNO", required = false) Integer articleNO,
			HttpServletRequest request, RedirectAttributes rttr) {
		String me = currentUserId(request.getSession());
		if (me == null) {
			rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
			return new ModelAndView("redirect:/member/loginForm.do");
		}
		if (articleNO == null) {
			rttr.addFlashAttribute("msg", "잘못된 접근입니다. 글 번호가 없습니다.");
			return new ModelAndView("redirect:/support/myRequests.do");
		}

		java.util.List<com.myspring.eum.support.vo.SupportApplicationVO> list = supportService
				.listApplicantsByArticle(articleNO);

		ModelAndView mv = new ModelAndView("support/applicants");
		mv.addObject("applicants", (list != null) ? list : java.util.Collections.emptyList());
		mv.addObject("articleNO", articleNO);
		mv.addObject("me", me);
		return mv;
	}

	// =========================================================
	// [GET] 내 글들에 달린 전체 지원자 — myApplicants.jsp
	// =========================================================
	@RequestMapping(value = "/myApplicants.do", method = RequestMethod.GET)
	public ModelAndView myApplicants(HttpServletRequest request, RedirectAttributes rttr) {
		String me = currentUserId(request.getSession());
		if (me == null) {
			rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
			return new ModelAndView("redirect:/member/loginForm.do");
		}

		List<SupportApplicationVO> applicants = supportService.listApplicantsToOwner(me);

		ModelAndView mv = new ModelAndView("support/myApplicants");
		mv.addObject("applicants", applicants);
		mv.addObject("me", me);
		return mv;
	}

	// =========================================================
	// [GET] 내가 올린 요청 글 목록 — myRequests.jsp
	// =========================================================
	@RequestMapping(value = "/myRequests.do", method = RequestMethod.GET)
	public ModelAndView myRequests(HttpServletRequest request, RedirectAttributes rttr) {
		String me = currentUserId(request.getSession());
		if (me == null) {
			rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
			return new ModelAndView("redirect:/member/loginForm.do");
		}

		List<ArticleVO> requests = supportService.listMyRequests(me);

		ModelAndView mv = new ModelAndView("support/myRequests");
		mv.addObject("requests", requests);
		mv.addObject("me", me);
		return mv;
	}

	// [GET] 내가 지원한 내역 — myApplications.jsp
	@RequestMapping(value = "/myApplications.do", method = RequestMethod.GET)
	public ModelAndView myApplications(HttpServletRequest request, RedirectAttributes rttr) {
		String me = currentUserId(request.getSession());
		if (me == null) {
			rttr.addFlashAttribute("msg", "로그인이 필요합니다.");
			return new ModelAndView("redirect:/member/loginForm.do");
		}
		java.util.List<com.myspring.eum.support.vo.SupportApplicationVO> list = supportService.listMyApplications(me);

		ModelAndView mv = new ModelAndView("support/myApplications");
		mv.addObject("applications", (list != null) ? list : java.util.Collections.emptyList());
		mv.addObject("me", me);
		return mv;
	}

}
