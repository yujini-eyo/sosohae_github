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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
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
@RequestMapping("/board")
public class BoardControllerImpl implements BoardController {

    @Value("${article.image.repo:C:/eum_upload/article_image}")
    private String ARTICLE_IMAGE_REPO;

    @Autowired
    private BoardService boardService;

    /*
    // 필요시만 사용
    @PostConstruct
    public void ensureRepo() {
        new File(ARTICLE_IMAGE_REPO).mkdirs();
        new File(ARTICLE_IMAGE_REPO + File.separator + "temp").mkdirs();
    }
    */

    /** 목록 */
    @Override
    @RequestMapping(value = "/listArticles.do", method = { RequestMethod.GET, RequestMethod.POST })
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
        // Tiles 미사용 시:
        //return new ModelAndView("forward:/WEB-INF/views/board/articleForm.jsp");
    }

    /** 글 등록 (VO 기반, PRG) */
    @Override
    @RequestMapping(value = "/addNewArticle.do", method = RequestMethod.POST)
    public String addNewArticle(
            @ModelAttribute("article") ArticleVO article,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            HttpServletRequest request,
            HttpSession session,
            RedirectAttributes rttr) throws Exception {

        // 로그인 사용자 세팅
        MemberVO memberVO = (session != null) ? (MemberVO) session.getAttribute("member") : null;
        String id = (memberVO != null && memberVO.getId() != null) ? memberVO.getId() : "guest";
        article.setId(id);

        // 기본값 보정
        if (article.getParentNO() == null) article.setParentNO(0);
        if (article.getPoints() == null) article.setPoints(0);
        if (article.getIsNotice() == null) article.setIsNotice(false);

        // 이미지 temp 저장
        String imageFileName = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imageFileName = saveToTemp(imageFile);
            article.setImageFileName(imageFileName);
        }

        try {
            int articleNO = boardService.addNewArticle(article); // useGeneratedKeys로 PK 채워짐

            // 이미지가 있으면 temp → {articleNO}/ 로 이동
            if (imageFileName != null && !imageFileName.isEmpty()) {
                File srcFile = new File(ARTICLE_IMAGE_REPO + File.separator + "temp" + File.separator + imageFileName);
                File destDir = new File(ARTICLE_IMAGE_REPO + File.separator + articleNO);
                FileUtils.moveFileToDirectory(srcFile, destDir, true);
            }
            rttr.addAttribute("msg", "등록되었습니다.");
            return "redirect:/board/listArticles.do";

        } catch (Exception e) {
            // 실패 시 temp 정리
            if (imageFileName != null && !imageFileName.isEmpty()) {
                File srcFile = new File(ARTICLE_IMAGE_REPO + File.separator + "temp" + File.separator + imageFileName);
                if (srcFile.exists()) srcFile.delete();
            }
            rttr.addAttribute("msg", "오류가 발생했습니다. 다시 시도해 주세요.");
            return "redirect:/board/articleForm.do";
        }
    }
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null) { setValue(null); return; }
                String t = text.trim();
                if (t.isEmpty()) { setValue(null); return; }

                try {
                    // 1) HTML5 datetime-local: "yyyy-MM-dd'T'HH:mm"
                    if (t.contains("T")) {
                        LocalDateTime ldt = LocalDateTime.parse(
                            t, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                        setValue(Timestamp.valueOf(ldt));
                        return;
                    }
                    // 2) 공백 구분: "yyyy-MM-dd HH:mm" or "yyyy-MM-dd HH:mm:ss"
                    if (t.contains(" ")) {
                        // "yyyy-MM-dd HH:mm"인 경우 초를 보정
                        if (t.length() == 16) t = t + ":00";
                        setValue(Timestamp.valueOf(t));
                        return;
                    }
                    // 3) 날짜만 온 경우: "yyyy-MM-dd" → 00:00:00
                    LocalDate d = LocalDate.parse(t, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    setValue(Timestamp.valueOf(d.atStartOfDay()));
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Invalid reqAt format: " + t);
                }
            }
        });
    }

    /** 상세보기 — (중요) 인터페이스와 동일하게 Integer 사용 */
    @Override
    @RequestMapping(value = "/viewArticle.do", method = RequestMethod.GET)
    public ModelAndView viewArticle(@RequestParam("articleNO") Integer articleNO,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        ArticleVO article = boardService.viewArticle(articleNO);
        ModelAndView mav = new ModelAndView("board/viewArticle"); // Tiles 정의명
        mav.addObject("article", article);

        String msg = request.getParameter("msg");
        if (msg != null && !msg.isEmpty()) mav.addObject("msg", msg);
        return mav;
    }

    /** 글 수정 (PRG) — 현 구조(Map) 유지 */
    @Override
    @RequestMapping(value = "/modArticle.do", method = RequestMethod.POST)
    public ModelAndView modArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
            throws Exception {
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
    @RequestMapping(value = "/removeArticle.do", method = RequestMethod.POST)
    public ModelAndView removeArticle(@RequestParam("articleNO") Integer articleNO,
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

    /** (바이너리) 이미지 보기 — 인터페이스 외 유틸 */
    @RequestMapping(value = "/image.do", method = RequestMethod.GET)
    public void image(@RequestParam("articleNO") Integer articleNO,
                      @RequestParam("file") String file,
                      HttpServletResponse resp) throws IOException {
        File target = new File(ARTICLE_IMAGE_REPO + File.separator + articleNO + File.separator + file);
        if (!target.exists()) {
            resp.setStatus(404);
            return;
        }
        resp.setContentType("image/*");
        try (FileInputStream fis = new FileInputStream(target);
             ServletOutputStream os = resp.getOutputStream()) {
            byte[] buf = new byte[8192];
            int len;
            while ((len = fis.read(buf)) != -1) os.write(buf, 0, len);
        }
    }

    /** 업로드 공통: temp 저장 (MultipartHttpServletRequest 버전) — 수정/구코드용 */
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

    /** 업로드 공통: temp 저장 (MultipartFile 단일 파라미터 버전) — 신규 등록용 */
    private String saveToTemp(MultipartFile mf) throws Exception {
        if (mf == null || mf.isEmpty()) return null;
        File tempDir = new File(ARTICLE_IMAGE_REPO + File.separator + "temp");
        if (!tempDir.exists()) tempDir.mkdirs();
        String imageFileName = mf.getOriginalFilename();
        if (imageFileName == null || imageFileName.isEmpty()) return null;
        mf.transferTo(new File(tempDir, imageFileName));
        return imageFileName;
    }

}
