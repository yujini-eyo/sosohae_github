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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller("boardAdminController")
@RequestMapping("/admin/board")
public class BoardAdminControllerImpl implements BoardAdminController {

    private static final Logger logger = LoggerFactory.getLogger(BoardAdminControllerImpl.class);

    @Autowired
    private BoardAdminService boardAdminService;

    /* 목록 */
    @RequestMapping(value="/adminList.do", method=RequestMethod.GET)
    public ModelAndView adminList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,Object> param = new HashMap<String,Object>();
        List<AdminArticleVO> list = boardAdminService.adminList(param);
        logger.debug("[admin] adminList -> {} rows", (list == null ? 0 : list.size()));
        ModelAndView mav = new ModelAndView("admin/board/adminList");
        mav.addObject("articlesList", list);
        return mav;
    }

    /* 상세 */
    @RequestMapping(value="/adminview.do", method=RequestMethod.GET)
    public ModelAndView adminview (HttpServletRequest request, HttpServletResponse response,
                                         @RequestParam("articleNO") int articleNO) throws Exception {
        AdminArticleVO vo = boardAdminService.adminselectArticleByNo(articleNO);
        logger.debug("[admin] adminview articleNO={} -> found? {}", articleNO, (vo != null));
        ModelAndView mav = new ModelAndView("admin/board/adminview");
        mav.addObject("article", vo);
        return mav;
    }

    /* 작성폼 */
    @RequestMapping(value="/adminWriteFrom.do", method=RequestMethod.GET)
    public ModelAndView adminWriteForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("admin/board/adminWriteForm");
    }

    /* 등록 - 일반 시그니처 */
    @RequestMapping(value="/adminaddNewArticle.do", method=RequestMethod.POST)
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
    @RequestMapping(value="/adminaddNewArticle.do", method=RequestMethod.POST, params="title")
    public ModelAndView adminaddNewArticleMultipart(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws Exception {
        request.setCharacterEncoding("UTF-8");
        logger.debug("[admin] addNewArticleMultipart() called, file? {}", (imageFile != null && !imageFile.isEmpty()));
        return handleAddCommon(request, imageFile);
    }

    /* 삭제 */
    @RequestMapping(value="/adminremoveArticle.do", method=RequestMethod.POST)
    public ModelAndView adminremoveArticle(HttpServletRequest request, HttpServletResponse response,
                                           @RequestParam("articleNO") int articleNO) throws Exception {
        boardAdminService.adminremoveArticle(articleNO);
        logger.debug("[admin] adminremoveArticle articleNO={} -> done", articleNO);
        return new ModelAndView("redirect:/admin/board/adminList.do");
    }

    /* 공지 on */
    @RequestMapping(value="/adminnoticeOn.do", method=RequestMethod.POST)
    public ModelAndView adminnoticeOn(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam("articleNO") int articleNO) throws Exception {
        boardAdminService.adminupdateNoticeFlag(articleNO, true);
        logger.debug("[admin] adminnoticeOn articleNO={}", articleNO);
        return new ModelAndView("redirect:/admin/board/adminList.do");
    }

    /* 공지 off */
    @RequestMapping(value="/adminnoticeOff.do", method=RequestMethod.POST)
    public ModelAndView adminnoticeOff(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam("articleNO") int articleNO) throws Exception {
        boardAdminService.adminupdateNoticeFlag(articleNO, false);
        logger.debug("[admin] adminnoticeOff articleNO={}", articleNO);
        return new ModelAndView("redirect:/admin/board/adminList.do");
    }

    // ---------- (호환용) 예전/잘못된 URL 별칭 ----------
    // 잘못된 경로만 새 경로로 리다이렉트 (중복 매핑 방지)
    // /admin/board/adminWriteForm(.do) -> /admin/board/adminWriteFrom.do
    @RequestMapping(value={"/adminWriteForm","/adminWriteForm.do"}, method=RequestMethod.GET)
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
}
