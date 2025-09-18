package com.myspring.eum.board.controller;

import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myspring.eum.board.service.BoardService;
import com.myspring.eum.board.vo.ArticleVO;
import com.myspring.eum.member.vo.MemberVO;
import com.myspring.eum.support.service.SupportService;

@Controller("boardController")
@RequestMapping("/board")
public class BoardControllerImpl implements BoardController {

	@Value("${article.image.repo:C:/eum_upload/article_image}")
	private String ARTICLE_IMAGE_REPO;

	@Autowired
	private BoardService boardService;

	@Autowired
	private SupportService supportService;
	/*
	 * // 필요시만 사용
	 * 
	 * @PostConstruct public void ensureRepo() { new
	 * File(ARTICLE_IMAGE_REPO).mkdirs(); new File(ARTICLE_IMAGE_REPO +
	 * File.separator + "temp").mkdirs(); }
	 */

	/** 목록 */
	@Override
	@RequestMapping(value = "/listArticles.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<ArticleVO> articlesList = boardService.listArticles();
		ModelAndView mav = new ModelAndView("board/listArticles"); // Tiles 정의명
		mav.addObject("list", articlesList);

		String msg = request.getParameter("msg");
		if (msg != null && !msg.isEmpty())
			mav.addObject("msg", msg);

		return mav;
	}

	/** 이미지 목록(옵션) */
	@Override
	@RequestMapping(value = "/listImages.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView listImages(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<ArticleVO> imageFileList = boardService.listArticles();
		ModelAndView mav = new ModelAndView("board/listImages"); // Tiles 정의명
		mav.addObject("imageFileList", imageFileList);
		return mav;
	}

	/** 답글 폼(옵션) */
	@Override
	@RequestMapping(value = "/replyForm.do", method = RequestMethod.GET)
	public ModelAndView replyForm() {
		return new ModelAndView("board/replyForm"); // Tiles 정의명
	}

	/** 글쓰기 폼 */
	@Override
	@RequestMapping(value = "/articleForm.do", method = RequestMethod.GET)
	public ModelAndView articleForm() {
		return new ModelAndView("board/articleForm"); // Tiles 정의명
		// jsp 임시 화면 출력 확인용
		// return new ModelAndView("forward:/WEB-INF/views/board/articleForm.jsp");
	}

	/** 글 등록 (VO 기반, PRG: 등록 → 상세) */
	@Override
	@RequestMapping(value = "/addNewArticle.do", method = RequestMethod.POST)
	public String addNewArticle(@ModelAttribute("article") ArticleVO article,
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile, HttpServletRequest request,
			HttpSession session, RedirectAttributes rttr) throws Exception {

		System.out.println("[ADD] hit addNewArticle : " + article);

		// 로그인 사용자 세팅
		MemberVO memberVO = (session != null) ? (MemberVO) session.getAttribute("member") : null;
		String id = (memberVO != null && memberVO.getId() != null) ? memberVO.getId() : "guest";
		article.setId(id);

		// 기본값 보정
		if (article.getParentNO() == null)
			article.setParentNO(0);
		if (article.getPoints() == null)
			article.setPoints(0);
		if (article.getIsNotice() == null)
			article.setIsNotice(false);

		// 이미지 temp 저장
		String imageFileName = null;
		if (imageFile != null && !imageFile.isEmpty()) {
			imageFileName = saveToTemp(imageFile);
			article.setImageFileName(imageFileName);
		}

		try {
			// 로그인 보정
			MemberVO m = (session != null) ? (MemberVO) session.getAttribute("member") : null;
			article.setId((m != null && m.getId() != null) ? m.getId() : "guest");

			// ★ content 디버깅 + 방어
			System.out.println(
					"[ADD] content.len=" + (article.getContent() == null ? "null" : article.getContent().length()));
			if (article.getContent() == null)
				article.setContent(""); // 빈문자라도 보내기

			long newNo = boardService.addNewArticle(article);
			return "redirect:/board/viewArticle.do?articleNO=" + newNo;
		} catch (Exception e) {
			e.printStackTrace();
			rttr.addFlashAttribute("msg", "등록 중 오류가 발생했습니다.");
			return "redirect:/board/articleForm.do";
		}
	}

	/** 상세보기 — (중요) 인터페이스와 동일하게 Integer 사용 */
	@Override
	@RequestMapping(value = "/viewArticle.do", method = RequestMethod.GET)
	public ModelAndView viewArticle(@RequestParam("articleNO") Integer articleNO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// HttpSession은 request에서 꺼내 쓰면 됩니다.
		HttpSession session = request.getSession(false);

		ModelAndView mv = new ModelAndView("board/viewArticle");

		ArticleVO article = boardService.viewArticle(articleNO);
		if (article == null) {
			mv.setViewName("redirect:/board/listArticles.do");
			mv.addObject("msg", "존재하지 않는 글입니다.");
			return mv;
		}
		mv.addObject("article", article);

		// 로그인 사용자 id
		MemberVO member = (session != null) ? (MemberVO) session.getAttribute("member") : null;
		String me = (member != null) ? member.getId() : null;
		mv.addObject("me", me);

		// ✅ [여기에 추가] 이미 지원했는지 여부 (뷰에서 "지원하기" 버튼 노출 판단용)
		boolean alreadyApplied = (me != null) && supportService.exists(articleNO, me);
		mv.addObject("alreadyApplied", alreadyApplied);

		// ✅ [작성자일 때만] 신청자 목록 내려주기
		// 네 서비스 메서드가 listApplicants(articleNO, authorId) 라면 그대로,
		// 내가 제안했던 시그니처(listByArticle(articleNO))를 쓰면 그걸로 바꿔도 됩니다.
		if (me != null && me.equals(article.getId())) {
			//mv.addObject("applicants", supportService.listApplicants(articleNO, me));
		    mv.addObject("applicants", supportService.listByArticle(articleNO));
		}

		return mv;
	}

	/** 글 수정 (VO 기반, JRE 1.6 호환) */
	@RequestMapping(value = "/modArticle.do", // ✅ FIX: "/board/modArticle.do" → "/modArticle.do"
			method = RequestMethod.POST, produces = "text/html; charset=UTF-8")

	@ResponseBody
	public ResponseEntity<String> modArticle(@ModelAttribute("article") ArticleVO article,
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
			@RequestParam(value = "originalFileName", required = false) String originalFileName,
			HttpServletRequest request) {

		// TODO: 환경에 맞게 외부화(@Value) 권장
		final String REPO = "D:\\eum\\article_images";

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/html; charset=UTF-8");

		String newImageFileName = null;

		try {
			// 1) 파일 업로드 처리(있을 때만)
			if (imageFile != null && !imageFile.isEmpty()) {
				String orig = imageFile.getOriginalFilename();
				long now = System.currentTimeMillis();
				newImageFileName = now + "_" + (orig == null ? "upload.bin" : orig);

				File tempDir = new File(REPO + File.separator + "temp");
				if (!tempDir.exists()) {
					tempDir.mkdirs();
				}
				File tempFile = new File(tempDir, newImageFileName);
				imageFile.transferTo(tempFile);

				// VO에 파일명 세팅
				article.setImageFileName(newImageFileName);
			}

			// 2) DB 업데이트 (제목/내용/이미지파일명 등)
			// 구현체에 이미 존재하는 시그니처: updateArticle(ArticleVO) 사용.
			// :contentReference[oaicite:2]{index=2}
			boardService.modArticle(article);

			// 3) 파일 이동/정리
			if (newImageFileName != null && newImageFileName.length() > 0) {
				File src = new File(REPO + File.separator + "temp", newImageFileName);
				File artDir = new File(REPO + File.separator + article.getArticleNO());
				if (!artDir.exists()) {
					artDir.mkdirs();
				}
				File dest = new File(artDir, newImageFileName);
				// Java 6: commons-io로 이동
				org.apache.commons.io.FileUtils.moveFile(src, dest);

				// 기존 파일 삭제
				if (originalFileName != null && originalFileName.length() > 0) {
					File old = new File(artDir, originalFileName);
					if (old.exists()) {
						old.delete();
					}
				}
			}

			String ok = "<script>" + "alert('글을 수정했습니다.');" + "location.href='" + request.getContextPath()
					+ "/board/viewArticle.do?articleNO=" + article.getArticleNO() + "';" + "</script>";
			return new ResponseEntity<String>(ok, headers, HttpStatus.OK);

		} catch (Exception ex) {
			// 실패 시 temp 정리
			try {
				if (newImageFileName != null && newImageFileName.length() > 0) {
					File src = new File(REPO + File.separator + "temp", newImageFileName);
					if (src.exists())
						src.delete();
				}
			} catch (Exception ignore) {
			}

			String fail = "<script>" + "alert('오류가 발생했습니다. 다시 수정해주세요.');" + "history.back();" + "</script>";
			return new ResponseEntity<String>(fail, headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/** 글 수정을 위한 바인더 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// ✅ Java6 호환: SimpleDateFormat 기반 (java.time.* 제거)
		binder.registerCustomEditor(java.sql.Timestamp.class, new java.beans.PropertyEditorSupport() {
			public void setAsText(String text) throws IllegalArgumentException {
				if (text == null) {
					setValue(null);
					return;
				}
				String t = text.trim();
				if (t.length() == 0) {
					setValue(null);
					return;
				}

				java.util.Date date = null;
				java.text.ParsePosition pos;

				// yyyy-MM-dd HH:mm:ss
				java.text.SimpleDateFormat f1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				f1.setLenient(false);
				pos = new java.text.ParsePosition(0);
				date = f1.parse(t, pos);
				if (date != null && pos.getIndex() == t.length()) {
					setValue(new java.sql.Timestamp(date.getTime()));
					return;
				}

				// yyyy-MM-dd HH:mm
				java.text.SimpleDateFormat f2 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
				f2.setLenient(false);
				pos = new java.text.ParsePosition(0);
				date = f2.parse(t, pos);
				if (date != null && pos.getIndex() == t.length()) {
					setValue(new java.sql.Timestamp(date.getTime()));
					return;
				}

				// yyyy-MM-dd
				java.text.SimpleDateFormat f3 = new java.text.SimpleDateFormat("yyyy-MM-dd");
				f3.setLenient(false);
				pos = new java.text.ParsePosition(0);
				date = f3.parse(t, pos);
				if (date != null && pos.getIndex() == t.length()) {
					setValue(new java.sql.Timestamp(date.getTime()));
					return;
				}

				throw new IllegalArgumentException("Invalid reqAt format: " + t);
			}
		});
	}

	/** 글 삭제 (PRG) */
	@Override
	@RequestMapping(value = "/removeArticle.do", method = RequestMethod.POST)
	public ModelAndView removeArticle(@RequestParam("articleNO") Integer articleNO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			boardService.removeArticle(articleNO);
			File destDir = new File(ARTICLE_IMAGE_REPO + File.separator + articleNO);
			if (destDir.exists())
				FileUtils.deleteDirectory(destDir);

			ModelAndView mav = new ModelAndView("redirect:/board/listArticles.do");
			mav.addObject("msg", "삭제되었습니다.");
			return mav;

		} catch (Exception e) {
			ModelAndView mav = new ModelAndView("redirect:/board/listArticles.do");
			mav.addObject("msg", "작업 중 오류가 발생했습니다. 다시 시도해 주세요.");
			return mav;
		}
	}

	/** (바이너리) 이미지 보기 — 인터페이스 외 유틸 */
//    @RequestMapping(value = "/image.do", method = RequestMethod.GET)
//    public void image(@RequestParam("articleNO") Integer articleNO,
//                      @RequestParam("file") String file,
//                      HttpServletResponse resp) throws IOException {
//        File target = new File(ARTICLE_IMAGE_REPO + File.separator + articleNO + File.separator + file);
//        if (!target.exists()) {
//            resp.setStatus(404);
//            return;
//        }
//        resp.setContentType("image/*");
//        try (FileInputStream fis = new FileInputStream(target);
//             ServletOutputStream os = resp.getOutputStream()) {
//            byte[] buf = new byte[8192];
//            int len;
//            while ((len = fis.read(buf)) != -1) os.write(buf, 0, len);
//        }
//    }

	/** 업로드 공통: temp 저장 (MultipartHttpServletRequest 버전) — 수정/구코드용 */
	private String uploadToTemp(MultipartHttpServletRequest req) throws Exception {
		String imageFileName = null;
		Iterator<String> names = req.getFileNames();

		File tempDir = new File(ARTICLE_IMAGE_REPO + File.separator + "temp");
		if (!tempDir.exists())
			tempDir.mkdirs();

		while (names.hasNext()) {
			MultipartFile mf = req.getFile(names.next());
			if (mf == null || mf.isEmpty())
				continue;
			imageFileName = mf.getOriginalFilename();
			if (imageFileName == null || imageFileName.isEmpty())
				continue;
			mf.transferTo(new File(tempDir, imageFileName));
		}
		return imageFileName;
	}

	/** 업로드 공통: temp 저장 (MultipartFile 단일 파라미터 버전) — 신규 등록용 */
	private String saveToTemp(MultipartFile mf) throws Exception {
		if (mf == null || mf.isEmpty())
			return null;
		File tempDir = new File(ARTICLE_IMAGE_REPO + File.separator + "temp");
		if (!tempDir.exists())
			tempDir.mkdirs();
		String imageFileName = mf.getOriginalFilename();
		if (imageFileName == null || imageFileName.isEmpty())
			return null;
		mf.transferTo(new File(tempDir, imageFileName));
		return imageFileName;
	}

}
