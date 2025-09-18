package com.myspring.eum.admin.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.myspring.eum.admin.service.BoardAdminService;
import com.myspring.eum.admin.vo.AdminArticleVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller("boardAdminController")
public class BoardAdminControllerImpl implements BoardAdminController {

    private static final Logger logger = LoggerFactory.getLogger(BoardAdminControllerImpl.class);

    @Autowired
    private BoardAdminService boardAdminService;

    /* 목록 */
    @RequestMapping(value="/admin/board/adminList.do", method=RequestMethod.GET)
    public ModelAndView adminList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        List<AdminArticleVO> list = boardAdminService.adminList(param);
        logger.debug("[admin] adminList -> {} rows", (list == null ? 0 : list.size()));
        ModelAndView mav = new ModelAndView("admin/board/adminList");
        mav.addObject("articlesList", list);
        return mav;
    }

    /* 상세 */
    @RequestMapping(value="/admin/board/adminview.do", method=RequestMethod.GET)
    public ModelAndView adminview (HttpServletRequest request, HttpServletResponse response,
                                         @RequestParam("articleNO") int articleNO) throws Exception {
        AdminArticleVO vo = boardAdminService.adminselectArticleByNo(articleNO);
        logger.debug("[admin] adminview articleNO={} -> found? {}", articleNO, (vo != null));
        ModelAndView mav = new ModelAndView("admin/board/adminview");
        mav.addObject("article", vo);
        return mav;
    }

    /* 작성폼 */
    @RequestMapping(value="/admin/board/adminWriteFrom.do", method=RequestMethod.GET)
    public ModelAndView adminWriteForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("admin/board/adminWriteForm");
    }

    /* 등록 - 일반 시그니처 */
    @RequestMapping(value="/admin/board/adminaddNewArticle.do", method=RequestMethod.POST)
    public ModelAndView adminaddNewArticle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        MultipartFile imageFile = null;

        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;
            imageFile = mreq.getFile("imageFile");
            logger.debug("[admin] adminaddNewArticle() via MultipartHttpServletRequest, file? {}", (imageFile != null && !imageFile.isEmpty()));
        } else {
            logger.debug("[admin] adminaddNewArticle() non-multipart request");
        }

        return handleAddCommon(request, imageFile);
    }

    /* 등록 - Multipart 파라미터 시그니처 */
    @RequestMapping(value="/admin/board/adminaddNewArticle.do", method=RequestMethod.POST, params="title")
    public ModelAndView adminaddNewArticleMultipart(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws Exception {
        request.setCharacterEncoding("UTF-8");
        logger.debug("[admin] addNewArticleMultipart() called, file? {}", (imageFile != null && !imageFile.isEmpty()));
        return handleAddCommon(request, imageFile);
    }

    /* 삭제 */
    @RequestMapping(value="/admin/board/adminremoveArticle.do", method=RequestMethod.POST)
    public ModelAndView adminremoveArticle(HttpServletRequest request, HttpServletResponse response,
                                           @RequestParam("articleNO") int articleNO) throws Exception {
        boardAdminService.adminremoveArticle(articleNO);
        logger.debug("[admin] adminremoveArticle articleNO={} -> done", articleNO);
        return new ModelAndView("redirect:/admin/board/adminList.do");
    }
    
    /* 삭제시 번호 재배열 예시(주석) */

    /* 공지 on */
    @RequestMapping(value="/admin/board/adminnoticeOn.do", method=RequestMethod.POST)
    public ModelAndView adminnoticeOn(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam("articleNO") int articleNO) throws Exception {
        boardAdminService.adminupdateNoticeFlag(articleNO, true);
        logger.debug("[admin] adminnoticeOn articleNO={}", articleNO);
        return new ModelAndView("redirect:/admin/board/adminList.do");
    }

    /* 공지 off */
    @RequestMapping(value="/admin/board/adminnoticeOff.do", method=RequestMethod.POST)
    public ModelAndView adminnoticeOff(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam("articleNO") int articleNO) throws Exception {
        boardAdminService.adminupdateNoticeFlag(articleNO, false);
        logger.debug("[admin] adminnoticeOff articleNO={}", articleNO);
        return new ModelAndView("redirect:/admin/board/adminList.do");
    }

    // ---------- (호환용) 잘못된 URL 별칭 ----------
    @RequestMapping(value={"/admin/board/adminWriteForm","/admin/board/adminWriteForm.do"}, method=RequestMethod.GET)
    public ModelAndView adminWriteFormAlias() {
        return new ModelAndView("redirect:/admin/board/adminWriteFrom.do");
    }

    // ---------- 공통 처리 ----------
    private ModelAndView handleAddCommon(HttpServletRequest request, MultipartFile imageFile) throws Exception {
        String title   = nvl(request.getParameter("title"));
        String content = nvl(request.getParameter("content"));
        String parent  = request.getParameter("parentNO");
        String isNoticeParam = request.getParameter("isNotice");

        if (title.length() == 0 || content.length() == 0) {
            ModelAndView mav = new ModelAndView("admin/board/adminWriteForm");
            mav.addObject("formError", "제목과 내용을 입력해 주세요.");
            mav.addObject("title", title);
            mav.addObject("content", content);
            logger.debug("[admin] save blocked: title/content missing");
            return mav;
        }

        Integer parentNO = null;
        try {
            if (parent != null && parent.trim().length() > 0) {
                parentNO = Integer.valueOf(Integer.parseInt(parent));
            }
        } catch (Exception ignore) { parentNO = null; }

        Boolean isNotice = Boolean.FALSE;
        if (isNoticeParam != null && isNoticeParam.trim().length() > 0) {
            isNotice = Boolean.TRUE;
        }

        String writerId = "admin";
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object sid = session.getAttribute("id");
            if (sid instanceof String && ((String) sid).length() > 0) {
                writerId = (String) sid;
            }
        }

        String savedFileName = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                savedFileName = storeFile(request, imageFile);
            } catch (IOException ioe) {
                logger.error("이미지 저장 실패: {}", ioe.getMessage(), ioe);
            }
        }

        AdminArticleVO vo = new AdminArticleVO();
        vo.setParentNO(parentNO);
        vo.setTitle(title);
        vo.setContent(content);
        vo.setId(writerId);
        vo.setImageFileName(savedFileName);
        vo.setIsNotice(isNotice);

        logger.debug(
            "[admin] save article - title='{}', writer='{}', notice={}, hasImage={}",
            new Object[]{ title, writerId, isNotice, Boolean.valueOf(savedFileName != null) }
        );

        boardAdminService.adminaddNewArticle(vo);

        return new ModelAndView("redirect:/admin/board/adminList.do");
    }

    private static String nvl(String s) { return (s == null) ? "" : s; }

    private String storeFile(HttpServletRequest request, MultipartFile file) throws IOException {
        String original = file.getOriginalFilename();
        String base = (original == null ? "upload" : original).replace('\\', '/');
        base = base.substring(base.lastIndexOf('/') + 1);
        String ext = "";
        int dot = base.lastIndexOf('.');
        if (dot > -1) ext = base.substring(dot);
        String name = System.currentTimeMillis() + "_" + Math.abs(base.hashCode()) + ext;

        ServletContext sc = request.getSession().getServletContext();
        String uploadDir = sc.getRealPath("/resources/adminboardImages");
        if (uploadDir == null) {
            uploadDir = new File(".").getAbsolutePath();
        }
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        File dest = new File(dir, name);
        try {
            file.transferTo(dest);
        } catch (IllegalStateException e) {
            throw new IOException(e.getMessage(), e);
        }
        return name;
    }

    /* ================== 공지 목록(공개) ================== */

    /** 주 경로: /eum/notice.do (컨텍스트가 /eum일 때) */
    @RequestMapping(value = {"/notice", "/notice/", "/notice.do"}, method = RequestMethod.GET)
    public ModelAndView noticeList(HttpServletRequest request,
                                   HttpServletResponse response,
                                   Model model) throws Exception {

        String q       = request.getParameter("q");
        String sort    = request.getParameter("sort");
        String pageStr = request.getParameter("page");
        String sizeStr = request.getParameter("size");

        int page = 1;
        try { if (pageStr != null && pageStr.length() > 0) page = Integer.parseInt(pageStr); } catch (Exception ignore) {}
        if (page < 1) page = 1;

        int size = 10;
        try { if (sizeStr != null && sizeStr.length() > 0) size = Integer.parseInt(sizeStr); } catch (Exception ignore) {}
        if (size < 1) size = 10;         // 최소 1
        if (size > 100) size = 100;      // 과도한 페이지 크기 방지

        if (sort == null || sort.isEmpty()) sort = "recent";

        List<AdminArticleVO> noticeList = boardAdminService.getNoticeList(q, sort, page, size);
        int total = boardAdminService.getNoticeTotal(q);

        model.addAttribute("noticeList", noticeList);
        model.addAttribute("total", Integer.valueOf(total));
        model.addAttribute("page",  Integer.valueOf(page));
        model.addAttribute("size",  Integer.valueOf(size));
        model.addAttribute("q",     q == null ? "" : q);
        model.addAttribute("sort",  sort);

        // /WEB-INF/views/notice.jsp
        return new ModelAndView("notice");
    }

    /* 상세(공개): /notice/view, /notice/view.do */
    @RequestMapping(value = {"/notice/view", "/notice/view.do"}, method = RequestMethod.GET)
    public ModelAndView noticeView(@RequestParam("articleNO") int articleNO) throws Exception {
        AdminArticleVO vo = boardAdminService.adminselectArticleByNo(articleNO);
        if (vo == null) {
            // 없으면 목록으로
            return new ModelAndView("redirect:/notice.do");
        }
        // 필요시 조회수 증가
        // boardAdminService.increaseViewCnt(articleNO);

        ModelAndView mav = new ModelAndView("noticeView"); // /WEB-INF/views/noticeView.jsp
        mav.addObject("article", vo);
        return mav; // ← 반드시 mav 반환
    }

    /* 목록(JSON): /notice/list.json */
    @RequestMapping(
        value = "/notice/list.json",
        method = RequestMethod.GET,
        produces = "application/json; charset=UTF-8"
    )
    @ResponseBody
    public Map<String, Object> noticeListJson(
            @RequestParam(value="q",     required=false) String q,
            @RequestParam(value="sort",  required=false, defaultValue="recent") String sort,
            @RequestParam(value="page",  required=false, defaultValue="1") int page,
            @RequestParam(value="size",  required=false, defaultValue="10") int size
    ) throws Exception {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        if (size > 100) size = 100;

        List<AdminArticleVO> items = boardAdminService.getNoticeList(q, sort, page, size);
        int total = boardAdminService.getNoticeTotal(q);

        Map<String,Object> res = new HashMap<>();
        res.put("items", items);
        res.put("total", total);
        res.put("page", page);
        res.put("size", size);
        res.put("sort", sort);
        res.put("q", q == null ? "" : q);
        return res;
    }

}
