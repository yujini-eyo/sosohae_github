// SupportController.java
// 역할: HTTP 요청 처리, 로그인 확인, 플래시 메시지, PRG 리다이렉트
package com.myspring.eum.support.controller;

import java.util.Collections;
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

@Controller("supportController")
@RequestMapping("/support")
public class SupportControllerImpl {

	@Autowired
	private SupportService supportService;

	@Autowired
	private BoardService boardService;

	// 세션에서 현재 로그인한 사용자 id 얻기 (프로젝트의 MemberVO 구조에 맞게 간단 구현)
	private String currentUserId(HttpSession session) {
		Object member = (session != null) ? session.getAttribute("member") : null;
		try {
			return (member != null) ? (String) member.getClass().getMethod("getId").invoke(member) : null;
		} catch (Exception ignore) {
			return null;
		}
	}

	// [POST] 지원하기
	@RequestMapping(value = "/apply.do", method = RequestMethod.POST)
	public String apply(@RequestParam int articleNO, @RequestParam(required = false) String message,
			HttpServletRequest request, RedirectAttributes rttr) {
		String me = currentUserId(request.getSession());
		if (me == null) { // 로그인 필요
			rttr.addFlashAttribute("msg", "로그인 후 지원할 수 있습니다.");
			return "redirect:/member/loginForm.do";
		}
		try {
			supportService.apply(articleNO, me, message);
			rttr.addFlashAttribute("msg", "지원이 접수되었습니다.");
		} catch (DuplicateKeyException e) { // 이미 지원한 경우
			rttr.addFlashAttribute("msg", "이미 지원한 글입니다.");
		}
		// PRG: 상세로 리다이렉트
		return "redirect:/board/viewArticle.do?articleNO=" + articleNO;
	}

	// [GET] 내 지원 목록
	@RequestMapping(value = "/myApplications.do", method = RequestMethod.GET)
	public ModelAndView myApplications(HttpServletRequest request) {
		String me = currentUserId(request.getSession());
		ModelAndView mv = new ModelAndView("support/myApplications");
		List<SupportApplicationVO> list = (me != null) ? supportService.listMyApplications(me)
				: java.util.Collections.<SupportApplicationVO>emptyList();
		mv.addObject("applications", list);
		return mv;
	}

	// [POST] 선정(글 작성자용 — 권한 체크는 JSP에서 작성자만 버튼 보이도록 처리)
	@RequestMapping(value = "/select.do", method = RequestMethod.POST)
	public String select(@RequestParam long applicationId, @RequestParam int articleNO, RedirectAttributes rttr) {
		supportService.select(applicationId);
		rttr.addFlashAttribute("msg", "지원자를 선정했습니다.");
		return "redirect:/board/viewArticle.do?articleNO=" + articleNO;
	}

	// [POST] 거절
	@RequestMapping(value = "/reject.do", method = RequestMethod.POST)
	public String reject(@RequestParam long applicationId, @RequestParam int articleNO, RedirectAttributes rttr) {
		supportService.reject(applicationId);
		rttr.addFlashAttribute("msg", "지원을 거절했습니다.");
		return "redirect:/board/viewArticle.do?articleNO=" + articleNO;
	}

	// [POST] 철회(지원자 본인)
	@RequestMapping(value = "/withdraw.do", method = RequestMethod.POST)
	public String withdraw(@RequestParam long applicationId, @RequestParam int articleNO, RedirectAttributes rttr) {
		supportService.withdraw(applicationId);
		rttr.addFlashAttribute("msg", "지원 신청을 취소했습니다.");
		return "redirect:/board/viewArticle.do?articleNO=" + articleNO;
	}

	// 지원자 목록 페이지
	@RequestMapping(value = "/applicants.do", method = RequestMethod.GET)
	public ModelAndView applicants(@RequestParam(value = "articleNO", required = false) Integer articleNO) {

		List<SupportApplicationVO> applications = (articleNO != null) ? supportService.listByArticle(articleNO)
				: Collections.<SupportApplicationVO>emptyList();
		if (applications == null)
			applications = Collections.<SupportApplicationVO>emptyList();

		ModelAndView mv = new ModelAndView("support/applicants");
		mv.addObject("applications", applications);
		mv.addObject("articleNO", articleNO);
		mv.addObject("hasParam", articleNO != null);
		return mv;
	}

	// 요청 내역 페이지
	@RequestMapping(value = "/myRequests.do", method = RequestMethod.GET)
	public ModelAndView myRequests(HttpServletRequest request) {
		List<ArticleVO> requests = Collections.<ArticleVO>emptyList(); // 더미 렌더링
		ModelAndView mv = new ModelAndView("support/myRequests"); // Tiles 이름 or JSP 논리명
		mv.addObject("requests", requests);
		mv.addObject("me", null);
		return mv;
	}
}
