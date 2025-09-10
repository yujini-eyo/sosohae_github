package com.myspring.eum.board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myspring.eum.board.service.BoardService;
import com.myspring.eum.board.vo.ArticleVO;
import com.myspring.eum.member.vo.MemberVO;

@Controller("boardController")
public class BoardControllerImpl implements BoardController {

    // app.properties : article.image.repo=C:/eum_upload/article_image
    @Value("${article.image.repo:C:/eum_upload/article_image}")
    private String ARTICLE_IMAGE_REPO;

    @Autowired
    private BoardService boardService;

    /* 필요시만 사용
    @PostConstruct
    public void ensureRepo() {
        new File(ARTICLE_IMAGE_REPO).mkdirs();
        new File(ARTICLE_IMAGE_REPO + File.separator + "temp").mkdirs();
    }
    */

    /** 목록 */
    @Override
    @RequestMapping(value = "/board/listArticles.do", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<ArticleVO> articlesList = boardService.listArticles();
        ModelAndView mav = new ModelAndView("board/listArticles"); // Tiles 정의명
        mav.addObject("articlesList", articlesList);

        String msg = request.getParameter("msg");
        if (msg != null && !msg.isEmpty()) mav.addObject("msg", msg);
        return mav;
    }

    /** 이미지 목록(옵션) */
    @Override
    @RequestMapping(value = "/board/listImages.do", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView listImages(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<ArticleVO> imageFileList = boardService.listArticles();
        ModelAndView mav = new ModelAndView("board/listImages"); // Tiles 정의명
        mav.addObject("imageFileList", imageFileList);
        return mav;
    }

    /** 답글 폼(옵션) */
    @Override
    @RequestMapping(value = "/board/replyForm.do", method = RequestMethod.GET)
    public ModelAndView replyForm() {
        return new ModelAndView("board/replyForm"); // Tiles 정의명
    }

    /** 글쓰기 폼 */
    @Override
    @RequestMapping(value = "/board/articleForm.do", method = RequestMethod.GET)
    public ModelAndView articleForm() {
        return new ModelAndView("board/articleForm"); // Tiles 정의명
    }

    /** 글 등록(단일 버전) — MultipartHttpServletRequest + Map → Service */
    @Override
    @RequestMapping(value = "/board/addNewArticle.do", method = RequestMethod.POST)
    public ModelAndView addNewArticle(MultipartHttpServletRequest multipartRequest,
                                      HttpServletResponse response) throws Exception {
        multipartRequest.setCharacterEncoding("utf-8");

        // 파라미터 → Map 수집
        Map<String, Object> articleMap = new HashMap<>();
        Enumeration<?> enu = multipartRequest.getParameterNames();
        while (enu.hasMoreElements()) {
            String name = (String) enu.nextElement();
            articleMap.put(name, multipartRequest.getParameter(name));
        }

        // 파일 업로드(temp)
        String imageFileName = uploadToTemp(multipartRequest);

        // 로그인 사용자 ID
        HttpSession session = multipartRequest.getSession(false);
        MemberVO memberVO = (session != null) ? (MemberVO) session.getAttribute("member") : null;
        String id = (memberVO != null && memberVO.getId() != null) ? memberVO.getId() : "guest";

        // 기본 필드
        articleMap.put("parentNO", 0); // 계층형이 아니면 0
        articleMap.put("id", id);
        articleMap.put("imageFileName", imageFileName);

        try {
            int articleNO = boardService.addNewArticle(articleMap);

            // 이미지가 있으면 temp → {articleNO}로 이동
            if (imageFileName != null && !imageFileName.isEmpty()) {
                File srcFile = new File(ARTICLE_IMAGE_REPO + File.separator + "temp" + File.separator + imageFileName);
                File destDir = new File(ARTICLE_IMAGE_REPO + File.separator + articleNO);
                FileUtils.moveFileToDirectory(srcFile, destDir, true);
            }

            ModelAndView mav = new ModelAndView("redirect:/board/listArticles.do");
            mav.addObject("msg", "등록되었습니다.");
            return mav;

        } catch (Exception e) {
            // 실패 시 temp 정리
            if (imageFileName != null && !imageFileName.isEmpty()) {
                File srcFile = new File(ARTICLE_IMAGE_REPO + File.separator + "temp" + File.separator + imageFileName);
                if (srcFile.exists()) srcFile.delete();
            }
            ModelAndView mav = new ModelAndView("redirect:/board/articleForm.do");
            mav.addObject("msg", "오류가 발생했습니다. 다시 시도해 주세요.");
            return mav;
        }
    }

    /** 상세보기 */
    @Override
    @RequestMapping(value = "/board/viewArticle.do", method = RequestMethod.GET)
    public ModelAndView viewArticle(@RequestParam("articleNO") long articleNO,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        ArticleVO article = boardService.viewArticle(articleNO);
        ModelAndView mav = new ModelAndView("board/viewArticle"); // Tiles 정의명
        mav.addObject("article", article);

        String msg = request.getParameter("msg");
        if (msg != null && !msg.isEmpty()) mav.addObject("msg", msg);
        return mav;
    }

    /** 글 수정 (PRG) */
    @Override
    @RequestMapping(value = "/board/modArticle.do", method = RequestMethod.POST)
    public ModelAndView modArticle(MultipartHttpServletRequest multipartRequest,
                                   HttpServletResponse response) throws Exception {
        multipartRequest.setCharacterEncoding("utf-8");

        Map<String, Object> articleMap = new HashMap<>();
        Enumeration<?> enu = multipartRequest.getParameterNames();
        while (enu.hasMoreElements()) {
            String name = (String) enu.nextElement();
            articleMap.put(name, multipartRequest.getParameter(name));
        }

        String imageFileName = uploadToTemp(multipartRequest);
        if (imageFileName != null && !imageFileName.isEmpty()) {
            articleMap.put("imageFileName", imageFileName);
        }

        String articleNO = (String) articleMap.get("articleNO");

        try {
            boardService.modArticle(articleMap);

            if (imageFileName != null && !imageFileName.isEmpty()) {
                File srcFile = new File(ARTICLE_IMAGE_REPO + File.separator + "temp" + File.separator + imageFileName);
                File destDir = new File(ARTICLE_IMAGE_REPO + File.separator + articleNO);
                FileUtils.moveFileToDirectory(srcFile, destDir, true);

                String originalFileName = (String) articleMap.get("originalFileName");
                if (originalFileName != null && !originalFileName.isEmpty()) {
                    File oldFile = new File(ARTICLE_IMAGE_REPO + File.separator + articleNO + File.separator + originalFileName);
                    if (oldFile.exists()) oldFile.delete();
                }
            }

            ModelAndView mav = new ModelAndView("redirect:/board/viewArticle.do");
            mav.addObject("articleNO", articleNO);
            mav.addObject("msg", "수정되었습니다.");
            return mav;

        } catch (Exception e) {
            if (imageFileName != null && !imageFileName.isEmpty()) {
                File srcFile = new File(ARTICLE_IMAGE_REPO + File.separator + "temp" + File.separator + imageFileName);
                if (srcFile.exists()) srcFile.delete();
            }
            ModelAndView mav = new ModelAndView("redirect:/board/viewArticle.do");
            mav.addObject("articleNO", articleNO);
            mav.addObject("msg", "오류가 발생했습니다. 다시 수정해 주세요.");
            return mav;
        }
    }

    /** 글 삭제 (PRG) */
    @Override
    @RequestMapping(value = "/board/removeArticle.do", method = RequestMethod.POST)
    public ModelAndView removeArticle(@RequestParam("articleNO") long articleNO,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        try {
            boardService.removeArticle(articleNO);
            File destDir = new File(ARTICLE_IMAGE_REPO + File.separator + articleNO);
            if (destDir.exists()) FileUtils.deleteDirectory(destDir);

            ModelAndView mav = new ModelAndView("redirect:/board/listArticles.do");
            mav.addObject("msg", "삭제되었습니다.");
            return mav;

        } catch (Exception e) {
            ModelAndView mav = new ModelAndView("redirect:/board/listArticles.do");
            mav.addObject("msg", "작업 중 오류가 발생했습니다. 다시 시도해 주세요.");
            return mav;
        }
    }

    /** (바이너리) 이미지 보기 */
    @RequestMapping(value = "/board/image.do", method = RequestMethod.GET)
    public void image(@RequestParam("articleNO") long articleNO,
                      @RequestParam("file") String file,
                      HttpServletResponse resp) throws IOException {
        File target = new File(ARTICLE_IMAGE_REPO + File.separator + articleNO + File.separator + file);
        if (!target.exists()) { resp.setStatus(404); return; }
        resp.setContentType("image/*");
        try (FileInputStream fis = new FileInputStream(target);
             ServletOutputStream os = resp.getOutputStream()) {
            byte[] buf = new byte[8192]; int len;
            while ((len = fis.read(buf)) != -1) os.write(buf, 0, len);
        }
    }

    /** 업로드 공통: temp 저장 */
    private String uploadToTemp(MultipartHttpServletRequest req) throws Exception {
        String imageFileName = null;
        Iterator<String> names = req.getFileNames();

        File tempDir = new File(ARTICLE_IMAGE_REPO + File.separator + "temp");
        if (!tempDir.exists()) tempDir.mkdirs();

        while (names.hasNext()) {
            MultipartFile mf = req.getFile(names.next());
            if (mf == null || mf.isEmpty()) continue;
            imageFileName = mf.getOriginalFilename();
            if (imageFileName == null || imageFileName.isEmpty()) continue;
            mf.transferTo(new File(tempDir, imageFileName));
        }
        return imageFileName;
    }

	@Override
	public String addNewArticle(ArticleVO article, MultipartFile imageFile, HttpServletRequest request,
			HttpSession session, RedirectAttributes rttr) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
